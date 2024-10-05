package org.nexus.subject;

/**
 * @author Xieningjun
 */
public class SubjectHolder {

    public SubjectState state;

    public long count;

    public Object result;

    public Throwable t;

    public SubjectHolder(SubjectState state, long count, Object result, Throwable throwable) {
        this.state = state;
        this.count = count;
        this.result = result;
        this.t = throwable;
    }

}
