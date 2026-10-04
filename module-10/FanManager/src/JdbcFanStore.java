import java.sql.*;

/** Reads and updates an existing fans table; never creates or deletes tables. */
public final class JdbcFanStore implements FanStore {
    private final Connection connection;

    public JdbcFanStore(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Fan find(int id) throws SQLException {
        try (PreparedStatement query = connection.prepareStatement(
                "SELECT ID, firstname, lastname, favoriteteam FROM fans WHERE ID = ?")) {
            query.setInt(1, id);
            query.setQueryTimeout(5);
            try (ResultSet result = query.executeQuery()) {
                if (!result.next()) return null;
                return new Fan(result.getInt("ID"), result.getString("firstname"),
                        result.getString("lastname"), result.getString("favoriteteam"));
            }
        }
    }

    @Override
    public boolean update(Fan fan) throws SQLException {
        try (PreparedStatement query = connection.prepareStatement(
                "UPDATE fans SET firstname = ?, lastname = ?, favoriteteam = ? WHERE ID = ?")) {
            query.setString(1, fan.firstName());
            query.setString(2, fan.lastName());
            query.setString(3, fan.favoriteTeam());
            query.setInt(4, fan.id());
            query.setQueryTimeout(5);
            return query.executeUpdate() == 1;
        }
    }
}
