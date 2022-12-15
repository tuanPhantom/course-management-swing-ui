package a2_1901040191.repository;

import a2_1901040191.repository.db.DbConnect;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Phan Quang Tuan
 * @version 1.0
 * @Overview A class that contains a map to manage sql statements to group multiple queries into a single unit and pass
 * it to the database in a network trip to the database.
 */
public class BatchManager {
    private static BatchManager instance;
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
            ps.executeBatch();
            batchMap.put(key, DbConnect.getConnection().prepareStatement(key));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
