package a2_1901040191.repository.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Phan Quang Tuan
 * @version 1.0
 * @Overview A class that provides connection to the database
 */
public class DbConnect {
    private static final String DB_URL = "jdbc:sqlite:database.sqlite3";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static Connection connection;

    private DbConnect() {
    }

    public synchronized static Connection getConnection() {
        try {
            if (connection == null || !connection.isValid(0)) {
                connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
