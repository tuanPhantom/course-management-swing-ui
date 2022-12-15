package a2_1901040191.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Phan Quang Tuan
 * @version 1.0
 * @Overview A class contains the thread pool that reuses a fixed number of threads operating off a shared unbounded
 * queue
 */
public class ThreadPool {
    public static final ExecutorService executor = Executors.newFixedThreadPool(8);

    private ThreadPool() {
    }
}
