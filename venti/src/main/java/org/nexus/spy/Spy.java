package org.nexus.spy;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Xieningjun
 */
public class Spy {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1 /* 改成初始化 */, Thread.ofVirtual().factory());

    private final Map<Future<?>, AtomicBoolean> cancelMap = new ConcurrentHashMap<>();

    private final PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

    public Spy() {
        scheduler.scheduleAtFixedRate(() -> {
            for (var entry : cancelMap.entrySet()) {
                var future = entry.getKey();
                var cancel = entry.getValue();
                if (cancel.get()) {
                    future.cancel(true);
                    cancelMap.remove(future);
                }
            }
        }, 0, 500, TimeUnit.MILLISECONDS);
        Metrics.addRegistry(registry);
    }

    public Thread one(String key, Runnable runnable) {
        return Thread.ofVirtual().start(new SpyRunnable(key, runnable));
    }

    public Future<?> loop(Runnable runnable, String key, long loop, long period) {
        var spyRunnable = new SpyRunnable(key, runnable);
        var loopRunnable = new LoopRunnable(loop, spyRunnable);
        var cancel = new AtomicBoolean(false);
        // 让其在一个任务开始后，即可马上开始下一个任务
        var future = scheduler.scheduleAtFixedRate(() -> {
            Thread.ofVirtual().start(() -> {
                try {
                    loopRunnable.run();
                } catch (LoopRunnable.LoopException e) {
                    cancel.set(true);
                }
            });
        }, 0, period, TimeUnit.MILLISECONDS);
        cancelMap.put(future, cancel);
        return future;
    }

    public void hang(Thread thread, Runnable runnable) {
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

    public void hang(Future<?> future, Runnable runnable) {
        Thread.ofVirtual().start(() -> {
            try {
                future.get();
            } catch (ExecutionException | InterruptedException e) {
                // todo
                throw new RuntimeException(e);
            } catch (CancellationException ignored) {
                /* 任务已取消 可以执行下一步了 */
            } finally {
                Thread.currentThread().interrupt();
            }
            runnable.run();
        });
    }

    public String scrape() {
        return registry.scrape();
    }

}
