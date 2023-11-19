package Other;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Person {
    private String name;

    public Person(String name) {
        this.name = name;
    }

//    public static Person fromResultSet(ResultSet resultSet) {
//        try {
//            return new Person(
//                    resultSet.getInt("id"),
//                    resultSet.getString("firstname"),
//                    resultSet.getInt("role_id")
//            );
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
