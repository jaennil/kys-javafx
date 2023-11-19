package Other;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthenticatedUser extends Person {
    private static AuthenticatedUser instance;
    private Integer id;
    private String idNumber;
    private Integer roleId;

    public AuthenticatedUser(Integer id, String idNumber, Integer roleId, String name) {
        super(name);
        this.id = id;
        this.idNumber = idNumber;
        this.roleId = roleId;
    }

    public static synchronized AuthenticatedUser getInstance() {
        if (instance == null) {
            instance = new AuthenticatedUser(null, null, null, null);
        }
        return instance;
    }

    public static void initFromResultSet(ResultSet resultSet) {
        try {
            instance = new AuthenticatedUser(
                    resultSet.getInt("id"),
                    resultSet.getString("idNumber"),
                    resultSet.getInt("role_id"),
                    resultSet.getString("name")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getRoleName() {
        Database database = Database.getInstance();
        String statement = "SELECT * FROM Role WHERE id = ?";
        Connection connection = database.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, roleId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("name");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "";
    }
}
