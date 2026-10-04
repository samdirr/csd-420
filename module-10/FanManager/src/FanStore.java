import java.sql.SQLException;

/** Sam Dirr — CSD 420 Module 10.2. Operations supported by the fan display. */
public interface FanStore {
    record Fan(int id, String firstName, String lastName, String favoriteTeam) { }
    Fan find(int id) throws SQLException;
    boolean update(Fan fan) throws SQLException;
}
