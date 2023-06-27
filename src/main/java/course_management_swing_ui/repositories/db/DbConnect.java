package course_management_swing_ui.repositories.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Phan Quang Tuan
 * @version 1.3
 * @Overview A class that provides connection to the database
 */
public class DbConnect {
    private static final String DB_URL = "jdbc:sqlite:database.sqlite3";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static final HikariDataSource dataSource;

    static {
        // Create HikariCP configuration
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(DB_URL);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);

        // Initialize the connection pool
        dataSource = new HikariDataSource(config);

        // set max size of pool = 4
        dataSource.setMaximumPoolSize(4);
    }

    private DbConnect() {
    }

    /**
     * Return a new connection to the database from the connection pool.
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
