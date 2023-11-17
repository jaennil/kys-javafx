package Other;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Pet {
    private int id;
    private String name;
    private int breed_id;
    private int owner_id;

    public Pet(int id, String name, int breed_id, int owner_id) {
        this.id = id;
        this.name = name;
        this.breed_id = breed_id;
        this.owner_id = owner_id;
    }

    public Pet(String name, int breed_id, int owner_id) {
        this.name = name;
        this.breed_id = breed_id;
        this.owner_id = owner_id;
    }

    public static Pet fromResultSet(ResultSet resultSet) {
        try {
            return new Pet(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getInt("breed_id"),
                    resultSet.getInt("owner_id")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    public int getBreedId() {
        return breed_id;
    }

    public void setBreedId(int breed_id) {
        this.breed_id = breed_id;
    }

    public int getOwnerId() {
        return owner_id;
    }

    public void setOwnerId(int owner_id) {
        this.owner_id = owner_id;
    }

    public String getBreedName() {
        Database database = Database.getInstance();
        Connection connection = database.getConnection();
        String statement = "select * from breeds where id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setInt(1, breed_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next())
                throw new RuntimeException();
            return resultSet.getString("name");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
