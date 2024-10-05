package org.nexus.virtual;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ContinuousVirtualThreadLoadTest {

    private static final int TOTAL_REQUESTS_PER_SECOND = 1000;

    public static void main(String[] args) {

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newVirtualThreadPerTaskExecutor();

        List<Long> costs = new CopyOnWriteArrayList<>();

        Thread.ofVirtual().start(() -> {
            while (true) {
                System.out.println(costs.getLast());
                try {
                    Thread.sleep(333);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        while (true) {

            for (int i = 0; i < TOTAL_REQUESTS_PER_SECOND; i++) {
                long start = System.currentTimeMillis();
                Runnable task = () -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        long end = System.currentTimeMillis();
                        costs.add(end - start);
                    }
                };
                executor.submit(task);
            }

            // 等待1秒，确保每秒只提交1000个请求
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
