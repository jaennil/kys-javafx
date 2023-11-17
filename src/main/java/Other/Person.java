package Other;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Person {
    private int id;
    private String firstname;
    private String surname;
    private String lastname;
    private String address;
    private String phoneNumber;
    private String username;
    private String role;

    public Person(Integer id, String firstname, String surname, String lastname, String address, String phoneNumber, String username, String role) {
        this.id = id;
        this.firstname = firstname;
        this.surname = surname;
        this.lastname = lastname;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.role = role;
    }

    public Person(String firstname, String surname, String lastname, String address, String phoneNumber, String username, String role) {
        this.firstname = firstname;
        this.surname = surname;
        this.lastname = lastname;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFullName() {
        return surname + " " + firstname + " " + lastname;
    }

    public String getWelcomeName() {
        return firstname + " " + lastname;
    }

    @Override
    public String toString() {
        return getFullName();
    }

    public static Person fromResultSet(ResultSet resultSet) {
        try {
            return new Person(
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
