import java.sql.*;

/** Uses a transaction and rollback so test records and edits are not retained. */
public final class JdbcFanStoreTest {
    private static int checks;
    static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
        checks++;
    }
    public static void main(String[] args) throws Exception {
        String password = System.getenv("CSD420_DB_PASSWORD");
        if (password == null) throw new IllegalStateException("Set CSD420_DB_PASSWORD to the assignment password.");
        String url = System.getProperty("fan.db.url");
        try (Connection connection = DriverManager.getConnection(url,"student1",password)) {
            try (PreparedStatement query = connection.prepareStatement(
                    "SELECT ENGINE FROM information_schema.TABLES WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'fans'");
                 ResultSet result = query.executeQuery()) {
                if (!result.next() || !"InnoDB".equalsIgnoreCase(result.getString(1)))
                    throw new IllegalStateException("Integration tests require an existing InnoDB fans table for rollback.");
            }
            connection.setAutoCommit(false);
            int id = Integer.MIN_VALUE;
            JdbcFanStore store = new JdbcFanStore(connection);
            try {
                // Choose unused IDs without touching existing fan records.
                while (store.find(id) != null || store.find(id + 1) != null) id += 2;
                check(store.find(id) == null, "Missing ID returns null");
                try (PreparedStatement insert = connection.prepareStatement(
                        "INSERT INTO fans (ID,firstname,lastname,favoriteteam) VALUES (?, 'Test', 'Fan', 'Cubs')")) {
                    insert.setInt(1,id); insert.executeUpdate();
                    insert.setInt(1,id+1); insert.executeUpdate();
                }
                FanStore.Fan original = store.find(id);
                check(original.equals(new FanStore.Fan(id,"Test","Fan","Cubs")), "Read all columns");
                FanStore.Fan edited = new FanStore.Fan(id,"O'Neil", "x'); --", "Bulls");
                check(store.update(edited), "Update returns success");
                check(store.find(id).equals(edited), "Apostrophes and SQL-like text stored literally");
                check(store.find(id+1).favoriteTeam().equals("Cubs"), "Other fan unchanged");
                check(store.update(edited), "Unchanged update still succeeds");
                FanStore.Fan boundary = new FanStore.Fan(id,"x".repeat(25),"",null);
                check(store.update(boundary) && store.find(id).equals(boundary), "Boundary, empty, and null values");
            } finally { connection.rollback(); }
            check(store.find(id) == null && store.find(id+1) == null, "Rollback removes test records");
            check(!store.update(new FanStore.Fan(id,"Missing","Fan","Team")), "Missing update returns false");
            connection.rollback();
        }
        System.out.println("PASS: " + checks + " MySQL integration checks; test changes rolled back.");
    }
}
