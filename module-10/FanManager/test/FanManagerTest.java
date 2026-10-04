import java.sql.SQLException;
import javax.swing.SwingUtilities;

/** Automated interface tests click the real Swing buttons on the event thread. */
public final class FanManagerTest {
    private static int checks;
    static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
        checks++;
    }
    private static final class FakeStore implements FanStore {
        Fan fan = new Fan(1, "Sam", "Dirr", "Cubs");
        boolean fail;
        int writes;
        public Fan find(int id) throws SQLException {
            if (fail) throw new SQLException("Simulated database outage");
            return fan != null && id == fan.id() ? fan : null;
        }
        public boolean update(Fan value) throws SQLException {
            if (fail) throw new SQLException("Simulated database outage");
            writes++;
            if (fan == null) return false;
            fan = value;
            return true;
        }
    }
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            FakeStore store = new FakeStore();
            FanManager ui = new FanManager(store);
            check(!ui.updateButton.isEnabled(), "Update initially disabled");
            check(!ui.firstNameField.isEditable(), "Fields initially protected");
            for (String invalid : new String[]{"", "abc", "1.5", "2147483648", "1 OR 1=1"}) {
                ui.idField.setText(invalid);
                ui.displayButton.doClick();
                check(ui.status.getText().contains("valid whole-number"), "Reject invalid ID " + invalid);
            }
            check(FanManager.parseId(" -2147483648 ") == Integer.MIN_VALUE, "Minimum integer ID");
            check(FanManager.parseId("2147483647") == Integer.MAX_VALUE, "Maximum integer ID");
            ui.idField.setText(" 1 ");
            ui.displayButton.doClick();
            check(ui.firstNameField.getText().equals("Sam"), "Display first name");
            check(ui.lastNameField.getText().equals("Dirr"), "Display last name");
            check(ui.teamField.getText().equals("Cubs"), "Display favorite team");
            check(ui.updateButton.isEnabled() && ui.teamField.isEditable(), "Enable editing after Display");
            ui.firstNameField.setText("Taylor");
            ui.lastNameField.setText("O'Neil");
            ui.teamField.setText("Bulls");
            ui.updateButton.doClick();
            check(store.fan.equals(new FanStore.Fan(1,"Taylor","O'Neil","Bulls")), "Update all fields");
            check(ui.status.getText().contains("successfully"), "Update success feedback");
            ui.displayButton.doClick();
            check(ui.teamField.getText().equals("Bulls"), "Redisplay updated values");
            ui.idField.setText("2");
            ui.updateButton.doClick();
            check(store.writes == 1 && ui.status.getText().contains("Click Display"), "Changed ID cannot overwrite another record");
            ui.idField.setText("bad");
            ui.updateButton.doClick();
            check(store.writes == 1 && ui.status.getText().contains("valid whole-number"), "Update rejects invalid ID");
            ui.idField.setText("1");
            javax.swing.JTextField[] fields = {ui.firstNameField, ui.lastNameField, ui.teamField};
            for (javax.swing.JTextField field : fields) {
                String before = field.getText();
                field.setText("x".repeat(26));
                ui.updateButton.doClick();
                check(store.writes == 1 && ui.status.getText().contains("25 characters"), "Reject oversized field");
                field.setText(before);
            }
            check(FanManager.validateText("x".repeat(25), "Name").length() == 25, "25-character boundary");
            check(FanManager.validateText("", "Name").isEmpty(), "Schema permits empty strings");
            check(FanManager.validateText("😀".repeat(25), "Name").codePointCount(0,50) == 25, "Unicode character length");
            store.fail = true;
            ui.updateButton.doClick();
            check(ui.status.getText().contains("were not saved"), "Update error feedback");
            store.fail = false;
            store.fan = null;
            ui.updateButton.doClick();
            check(ui.status.getText().contains("no longer exists") && !ui.updateButton.isEnabled(), "Deleted record handled");
            ui.displayButton.doClick();
            check(ui.status.getText().contains("No fan found") && ui.teamField.getText().isEmpty(), "Missing ID clears stale fields");
            store.fail = true;
            ui.displayButton.doClick();
            check(ui.status.getText().contains("could not display") && !ui.updateButton.isEnabled(), "Display error feedback");
            store.fail = false;
            store.fan = new FanStore.Fan(1,null,null,null);
            ui.displayButton.doClick();
            check(ui.firstNameField.getText().isEmpty(), "Nullable fields display safely");
            check(ui.updateButton.isEnabled(), "Display recovers after errors");
        });
        System.out.println("PASS: " + checks + " validation and Swing interface checks.");
    }
}
