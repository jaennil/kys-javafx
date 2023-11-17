package Other;

import java.awt.*;
import java.sql.*;

public class Appointment {
    private int id;
    private int pet_id;
    private int person_id;
    private int doctor_id;
    private Date date;

    public Appointment(int id, int pet_id, int person_id, int doctor_id, Date date) {
        this.id = id;
        this.pet_id = pet_id;
        this.person_id = person_id;
        this.doctor_id = doctor_id;
        this.date = date;
    }

    public Appointment(int pet_id, int person_id, int doctor_id, Date date) {
        this.pet_id = pet_id;
        this.person_id = person_id;
        this.doctor_id = doctor_id;
        this.date = date;
    }

    public static Appointment fromResultSet(ResultSet resultSet) {
        try {
            return new Appointment(
                    resultSet.getInt("id"),
                    resultSet.getInt("pet_id"),
                    resultSet.getInt("person_id"),
                    resultSet.getInt("doctor_id"),
                    resultSet.getDate("date")
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

    public int getPetId() {
        return pet_id;
    }

    public void setPetId(int pet_id) {
        this.pet_id = pet_id;
    }

    public int getPersonId() {
        return person_id;
    }

    public void setPersonId (int person_id) {
        this.person_id = person_id;
    }

    public int getDoctorId (){
        return doctor_id;
    }

    public void setDoctorId(int doctor_id) {
        this.doctor_id = doctor_id;
    }

    public Date getDate() {
        return date;
    }

    public String getDateString() {
        return date.toString();
    }

    public String getPetName() {
        Database database = Database.getInstance();
        Connection connection = database.getConnection();
        String statement = "select * from pets where id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setInt(1, pet_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next())
                throw new RuntimeException();
            return resultSet.getString("name");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getDoctorName() {
        Database database = Database.getInstance();
        Connection connection = database.getConnection();
        String statement = "select * from people where id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setInt(1, doctor_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next())
                throw new RuntimeException();
            Person doctor = Person.fromResultSet(resultSet);
            return doctor.getFullName();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
