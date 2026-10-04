import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

/** Sam Dirr — October 4, 2026 — CSD 420 Module 10.2.
 * Displays and updates existing fan records using their integer primary key.
 */
public final class FanManager extends JPanel {
    private final FanStore store;
    final JTextField idField = new JTextField(20);
    final JTextField firstNameField = new JTextField(20);
    final JTextField lastNameField = new JTextField(20);
    final JTextField teamField = new JTextField(20);
    final JButton displayButton = new JButton("Display");
    final JButton updateButton = new JButton("Update");
    final JLabel status = new JLabel("Enter an ID and click Display.");
    private Integer displayedId;

    public FanManager(FanStore store) {
        this.store = store;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JPanel fields = new JPanel(new GridLayout(4, 2, 10, 10));
        addField(fields, "ID", idField);
        addField(fields, "First Name", firstNameField);
        addField(fields, "Last Name", lastNameField);
        addField(fields, "Favorite Team", teamField);
        JPanel buttons = new JPanel();
        buttons.add(displayButton);
        buttons.add(updateButton);
        add(fields, BorderLayout.NORTH);
        add(buttons, BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);
        setRecordEditable(false);
        displayButton.addActionListener(event -> displayRecord());
        updateButton.addActionListener(event -> updateRecord());
    }

    private static void addField(JPanel panel, String text, JTextField field) {
        JLabel label = new JLabel(text + ":");
        label.setLabelFor(field);
        panel.add(label);
        panel.add(field);
    }

    private void setRecordEditable(boolean editable) {
        firstNameField.setEditable(editable);
        lastNameField.setEditable(editable);
        teamField.setEditable(editable);
        updateButton.setEnabled(editable);
    }

    static int parseId(String text) {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException("Enter a valid whole-number ID (32-bit integer).");
        }
    }

    static String validateText(String text, String label) {
        // MySQL VARCHAR lengths count characters, including supplementary Unicode.
        if (text.codePointCount(0, text.length()) > 25) {
            throw new IllegalArgumentException(label + " must be 25 characters or fewer.");
        }
        return text;
    }

    private void displayRecord() {
        displayedId = null;
        setRecordEditable(false);
        firstNameField.setText("");
        lastNameField.setText("");
        teamField.setText("");
        try {
            int id = parseId(idField.getText());
            FanStore.Fan fan = store.find(id);
            if (fan == null) {
                status.setText("No fan found with ID " + id + ".");
                return;
            }
            firstNameField.setText(fan.firstName());
            lastNameField.setText(fan.lastName());
            teamField.setText(fan.favoriteTeam());
            displayedId = id;
            setRecordEditable(true);
            status.setText("Displayed fan " + id + ". Edit the fields, then click Update.");
        } catch (IllegalArgumentException error) {
            status.setText(error.getMessage());
        } catch (SQLException error) {
            status.setText("Database error: could not display the record. Try again.");
            System.err.println("Display failed: " + error.getMessage());
        }
    }

    private void updateRecord() {
        try {
            int id = parseId(idField.getText());
            // Prevent an edited ID from overwriting another fan with stale fields.
            if (displayedId == null || id != displayedId) {
                status.setText("Click Display for this ID before updating.");
                return;
            }
            FanStore.Fan fan = new FanStore.Fan(id,
                    validateText(firstNameField.getText(), "First Name"),
                    validateText(lastNameField.getText(), "Last Name"),
                    validateText(teamField.getText(), "Favorite Team"));
            if (store.update(fan)) {
                status.setText("Updated fan " + id + " successfully.");
            } else {
                displayedId = null;
                setRecordEditable(false);
                status.setText("Record no longer exists. Click Display to load a record.");
            }
        } catch (IllegalArgumentException error) {
            status.setText(error.getMessage());
        } catch (SQLException error) {
            status.setText("Database error: changes were not saved. Try again.");
            System.err.println("Update failed: " + error.getMessage());
        }
    }

    public static void main(String[] args) {
        // Local setup uses 3307; override with -Dfan.db.url for another server.
        String url = System.getProperty("fan.db.url",
                "jdbc:mysql://127.0.0.1:3307/databasedb?useSSL=false&serverTimezone=UTC"
                + "&connectTimeout=5000&socketTimeout=5000&useAffectedRows=false");
        String password = System.getenv("CSD420_DB_PASSWORD");
        if (password == null) {
            JPasswordField input = new JPasswordField();
            int choice = JOptionPane.showConfirmDialog(null, input,
                    "Enter the assignment password for student1", JOptionPane.OK_CANCEL_OPTION);
            if (choice != JOptionPane.OK_OPTION) return;
            char[] characters = input.getPassword();
            password = new String(characters);
            java.util.Arrays.fill(characters, '\0');
        }
        try {
            Connection connection = DriverManager.getConnection(url, "student1", password);
            SwingUtilities.invokeLater(() -> {
                JFrame frame = new JFrame("Sam Dirr — CSD 420 Module 10.2 — Fan Manager");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setContentPane(new FanManager(new JdbcFanStore(connection)));
                frame.addWindowListener(new WindowAdapter() {
                    @Override public void windowClosed(WindowEvent event) {
                        try { connection.close(); }
                        catch (SQLException error) { System.err.println(error.getMessage()); }
                    }
                });
                frame.pack();
                frame.setMinimumSize(new Dimension(640, 290));
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            });
        } catch (SQLException error) {
            JOptionPane.showMessageDialog(null,
                    "Could not connect to databasedb. Check MySQL, port, driver, and password.",
                    "Database connection error", JOptionPane.ERROR_MESSAGE);
            System.err.println(error.getMessage());
        }
    }
}
