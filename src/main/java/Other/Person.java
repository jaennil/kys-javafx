package Other;

public class Person {
    private String fullname;

    public Person(String fullname) {
        this.fullname = fullname;
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
