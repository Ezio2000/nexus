package org.nexus.spy;

import org.nexus.ex.LoopException;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Xieningjun
 * @date 2024/9/30 15:42
 * @description
 */
public class LoopRunnable implements Runnable {

    private final long loop;

    private final AtomicLong innerCounter = new AtomicLong(0);

    private final AtomicLong outerCounter = new AtomicLong(0);

    private final AtomicBoolean loopFinished = new AtomicBoolean(false);

    private final Runnable delegate;

    LoopRunnable(long loop, Runnable delegate) {
        this.loop = loop;
        this.delegate = delegate;
    }

    // todo 有没有风险？
    @Override
    public final void run() {
        if (innerCounter.get() < loop) {
            innerCounter.incrementAndGet();
            delegate.run();
            outerCounter.incrementAndGet();
        } else {
            if (outerCounter.get() >= loop && loopFinished.compareAndSet(false, true)) {
                // todo 这里是否应该Runtime？
                throw new LoopException(loop, outerCounter.get());
            }
        }
    }

}
