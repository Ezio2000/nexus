package org.nexus.ex;

public class LoopException extends RuntimeException {

    public final long loop;

    public final long actual;

    public LoopException(long loop, long actual) {
        this.loop = loop;
        this.actual = actual;
    }

}
