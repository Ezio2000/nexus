package org.nexus.vir;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;

/**
 * @author Xieningjun
 */
public class SpyRunnable implements Runnable {

    private final String key;

    private String[] tags;

    private final Runnable delegate;

    SpyRunnable(Runnable delegate, String key) {
        this.delegate = delegate;
        this.key = key;
    }

    @Override
    public void run() {
        // 看看如何调参
        Timer timer = Timer.builder(key)
                .tags(tags)
                .publishPercentileHistogram(false)
                .publishPercentiles()
                .register(Metrics.globalRegistry);
        // 示例计时
        timer.record(delegate);
    }

    public void setTags(String... tags) {
        this.tags = tags;
    }

}
