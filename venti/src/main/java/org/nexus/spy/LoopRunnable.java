package org.nexus.spy;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Xieningjun
 * @date 2024/9/30 15:42
 * @description
 */
public class LoopRunnable implements Runnable {

    private final long loop;

    private final AtomicLong counter = new AtomicLong(0);

    private final Runnable delegate;

    LoopRunnable(long loop, Runnable delegate) {
        this.loop = loop;
        this.delegate = delegate;
    }

    @Override
    public void run() {
        if (counter.get() < loop) {
            counter.incrementAndGet();
            delegate.run();
        } else {
            throw new LoopException();
        }
    }

    public class LoopException extends RuntimeException {}

}
