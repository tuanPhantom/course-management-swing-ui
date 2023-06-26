package course_management_swing_ui.repository;

import course_management_swing_ui.repository.db.DbConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Phan Quang Tuan
 * @version 1.1
 * @Overview A class that contains a map to manage sql statements to group multiple queries into a single unit and pass
 * it to the database in a network trip to the database.
 */
public class BatchManager {
    private static BatchManager instance;
    private static Connection batchConnection = getBatchConnection();
    private final Map<String, PreparedStatement> batchMap;

    private BatchManager() {
        batchMap = new HashMap<>();
    }

    public static BatchManager getInstance() {
        if (instance == null) {
            instance = new BatchManager();
        }
        return instance;
    }

    /**
     * Return a new connection to the database from the connection pool, with the auto-commit set to false. This
     * connection is dedicated to use the batch. This method should be used before any call of getConnection() in the
     * program, and It should not be closed.
     */
    public synchronized static Connection getBatchConnection() {
        try {
            if (batchConnection == null || !batchConnection.isValid(0)) {
                batchConnection = DbConnect.getConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return batchConnection;
    }

    public PreparedStatement getBatch(String key) {
        return batchMap.get(key);
    }

    public void add(String key, PreparedStatement ps) {
        if (getBatch(key) == null) {
            batchMap.put(key, ps);
        }
    }

    public void executeBatch(String key) {
        PreparedStatement ps = getBatch(key);
        try {
            Connection conn = ps.getConnection();
            try {
                ps.executeBatch();
                conn.commit();
                batchMap.put(key, conn.prepareStatement(key));
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
