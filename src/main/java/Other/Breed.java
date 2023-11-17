package Other;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Breed {
    private int id;
    private String name;

    @Override
    public String toString() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Breed(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Breed fromResultSet(ResultSet resultSet) {
        try {
            return new Breed(
                    resultSet.getInt("id"),
                    resultSet.getString("name")
                    );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
