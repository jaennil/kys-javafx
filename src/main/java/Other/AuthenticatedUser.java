package Other;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthenticatedUser extends Person {
    private static AuthenticatedUser instance;
    private String idNumber;
    private String role;

    public AuthenticatedUser(String idNumber, String fullname, String role) {
        super(fullname);
        this.idNumber = idNumber;
        this.role = role;
    }

    public static synchronized AuthenticatedUser getInstance() {
        if (instance == null) {
            instance = new AuthenticatedUser(null, null, null);
        }
        return instance;
    }

    public static void initFromResultSet(ResultSet resultSet, String role) {
        try {
            instance = new AuthenticatedUser(
                    resultSet.getString("idNumber"),
                    resultSet.getString("fullname"),
                    role
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getRole() {
        return role;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }
}
