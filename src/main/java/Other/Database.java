package Other;

import java.sql.*;


public class Database {
    private static Database instance;
    private static Connection connection;
    private static final String url = "jdbc:mysql://pandora.lite-host.in:3306/tqhutcdy_kys";
    private static final String user = "tqhutcdy_jaennil";
    private static final String password = "naeNN6457";

    private Database() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException exception) {
            System.out.println("Database Connection Creation Failed : " + exception.getMessage());
        }
    }

    public static boolean connect() {
        System.out.println("connecting to database...");
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println("can't connect to database");
            return false;
        }
        System.out.println("successfully connected to database");
        return true;
    }

    public static Database getInstance() {
        try {
            if (instance == null)
                instance = new Database();
            else if (connection.isClosed())
                instance = new Database();
            return instance;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}