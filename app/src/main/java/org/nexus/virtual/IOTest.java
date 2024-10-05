package org.nexus.virtual;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

/**
 * @author Xieningjun
 * @date 2024/9/13 10:26
 * @description
 */
public class IOTest {

    public static void plat(int core, int concurrent, int mockTime) {
        var executor = new ThreadPoolExecutor(
                core, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS, new SynchronousQueue<>()
        );
        mock(concurrent, executor, mockTime);
        executor.shutdown();
        while (!executor.isTerminated()) {}
    }

    public static void virtual(int concurrent, int mockTime) {
        var executor = Executors.newVirtualThreadPerTaskExecutor();
        mock(concurrent, executor, mockTime);
        executor.shutdown();
        while (!executor.isTerminated()) {}
    }

    protected static void mock(int concurrent, ExecutorService executor, int mockTime) {
        IntStream.range(0, concurrent).forEach(i -> {
            executor.submit(() -> {
                try {
                    Thread.sleep(mockTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    public static long calculateAverage(List<Long> list) {
        long sum = list.stream().mapToLong(Long::longValue).sum();
        double average = (double) sum / list.size();
        return (long) average;
    }

    public static void main(String[] args) throws InterruptedException {
        int total = 0;
        int qps = 10000; // r/ms
        int mockSpent = 50; // ms
        int reqTime = 360; // s 可以理解为次数，也可以理解为秒，测试时间越长，结论越准确
        Thread.sleep(5000);
        long start = System.currentTimeMillis();
        var executor = Executors.newVirtualThreadPerTaskExecutor();
        while (total < reqTime) {
            IntStream.range(0, qps).forEach(i -> {
                executor.submit(() -> {
                    try {
                        Thread.sleep(mockSpent);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
            });
            total += 1;
            if (total < reqTime)
                Thread.sleep(1000);
        }
        executor.shutdown();
        if (!executor.awaitTermination((((reqTime - 1) * 1000) + mockSpent) + 60000, TimeUnit.MILLISECONDS)) {
            executor.shutdownNow();
        }
        long end = System.currentTimeMillis();
        long delay = (end - start) - ((reqTime - 1) * 1000 + mockSpent);
        System.out.println("Delay: " + delay);
    }

}
