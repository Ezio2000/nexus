package org.nexus;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Xieningjun
 */
public class Scheduler {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1, Thread.ofVirtual().factory());

    public static ScheduledFuture<?> schedule(Runnable runnable, long period, TimeUnit unit) {
        return scheduler.scheduleAtFixedRate(runnable, 0, period, unit);
    }

    public static ScheduledFuture<?> schedule(Runnable runnable, String key, long period, TimeUnit unit) {
        var spyRunnable = new SpyRunnable(key, runnable);
        return scheduler.scheduleAtFixedRate(spyRunnable, 0, period, unit);
    }

}
