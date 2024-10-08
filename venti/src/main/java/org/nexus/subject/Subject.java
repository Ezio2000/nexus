package org.nexus.subject;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Xieningjun
 */
public abstract class Subject implements SubjectRunnable {

    protected Object result = null;

    protected Throwable t = null;

    protected AtomicLong counter = new AtomicLong(0);

    protected AtomicReference<SubjectState> state = new AtomicReference<>(SubjectState.NOT_STARTED);

    @Override
    public abstract String key();

    public abstract Object run0() throws Throwable;

    public void afterResult(Object result) {}

    public void afterError(Throwable t) {}

    @Override
    public void before() {}

    @Override
    public final void run() {
        try {
            result = run0();
            state.set(SubjectState.RESULT);
        } catch (Throwable e) {
            t = e;
            state.set(SubjectState.ERROR);
        } finally {
            counter.getAndIncrement();
        }
    }

    @Override
    public final void after() {
        switch (state.get()) {
            case RESULT:
                afterResult(result);
                break;
            case ERROR:
                afterError(t);
                break;
            default:
                // loop且无状态的时候会不会导致卡死？（不执行？）
                break;
        }
    }

    @Override
    public final SubjectState state() {
        return state.get();
    }

    public final SubjectHolder holder() {
        return new SubjectHolder(state.get(), counter.get(), result, t);
    }

}
