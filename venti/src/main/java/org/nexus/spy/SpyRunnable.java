package org.nexus.spy;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;

/**
 * @author Xieningjun
 */
public class SpyRunnable implements Runnable {

    private final String key;

    private final Runnable delegate;

    SpyRunnable(String key, Runnable delegate) {
        this.key = key;
        this.delegate = delegate;
    }

    @Override
    public void run() {
        // 看看如何调参
        Timer timer = Timer.builder(key)
                .publishPercentileHistogram(false)
                .publishPercentiles()
                .register(Metrics.globalRegistry);
        // 示例计时
        timer.record(delegate);
    }

}
