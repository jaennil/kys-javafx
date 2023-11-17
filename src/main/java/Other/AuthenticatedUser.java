package Other;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthenticatedUser extends Person {
    private static AuthenticatedUser instance;

    public AuthenticatedUser(Integer id, String firstname, String surname, String lastname, String address, String phoneNumber, String username, String role) {
        super(id, firstname, surname, lastname, address, phoneNumber,username,role);
    }

    public static synchronized AuthenticatedUser getInstance() {
        if (instance == null) {
            instance = new AuthenticatedUser(null, null, null, null, null, null, null, null);
        }
        return instance;
    }

    public static void initFromResultSet(ResultSet resultSet) {
        try {
            instance = new AuthenticatedUser(
                    resultSet.getInt("id"),
                    resultSet.getString("firstname"),
                    resultSet.getString("surname"),
                    resultSet.getString("lastname"),
                    resultSet.getString("address"),
                    resultSet.getString("phone_number"),
                    resultSet.getString("username"),
                    resultSet.getString("role")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
