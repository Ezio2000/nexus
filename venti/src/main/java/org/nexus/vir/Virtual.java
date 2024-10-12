package org.nexus.vir;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.nexus.ex.LoopException;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Xieningjun
 */
public class Virtual {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1 /* 改成初始化 */, Thread.ofVirtual().factory());

    private static final Map<Future<?>, AtomicBoolean> cancelMap = new ConcurrentHashMap<>();

    private static final PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

    static  {
        scheduler.scheduleAtFixedRate(() -> {
            for (var entry : cancelMap.entrySet()) {
                var future = entry.getKey();
                var cancel = entry.getValue();
                if (cancel.get()) {
                    // todo true or false？
                    future.cancel(true);
                    cancelMap.remove(future);
                }
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
        Metrics.addRegistry(registry);
    }

    public static Thread one(Runnable runnable) {
        return Thread.ofVirtual().start(runnable);
    }

    public static Future<?> loop(Runnable runnable, long loop, long period) {
        var loopRunnable = new LoopRunnable(runnable, loop);
        var cancel = new AtomicBoolean(false);
        // 让其在一个任务开始后，即可马上开始下一个任务
        var future = scheduler.scheduleAtFixedRate(() -> {
            Thread.ofVirtual().start(() -> {
                try {
                    loopRunnable.run();
                } catch (LoopException e) {
                    System.out.println(Thread.currentThread() + ": loop - %s; actual - %s".formatted(e.loop, e.actual));
                    cancel.set(true);
                }
            });
        }, 0, period, TimeUnit.MILLISECONDS /* 也就是说 目前只支持到1000rps 如果要支持更高rps 需要改nano */);
        cancelMap.put(future, cancel);
        return future;
    }

    public static void hang(Thread thread, Runnable runnable) {
        Thread.ofVirtual().start(() -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                // todo
                throw new RuntimeException(e);
            } finally {
                Thread.currentThread().interrupt();
            }
            runnable.run();
        });
    }

    public static void hang(Future<?> future, Runnable runnable) {
        Thread.ofVirtual().start(() -> {
            try {
                future.get();
            } catch (ExecutionException | InterruptedException e) {
                // todo
                throw new RuntimeException(e);
            } catch (CancellationException ignored) {
                /* 任务已取消 可以执行下一步 */
            } finally {
                Thread.currentThread().interrupt();
            }
            runnable.run();
        });
    }

    public static Thread spy(Runnable runnable, String key) {
        return one(new SpyRunnable(runnable, key));
    }

    public static Future<?> spy(Runnable runnable, String key, long loop, long period) {
        return loop(new SpyRunnable(runnable, key), loop, period);
    }

    public static String scrape() {
        return registry.scrape();
    }

}
