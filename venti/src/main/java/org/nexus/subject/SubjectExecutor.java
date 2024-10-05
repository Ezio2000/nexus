package org.nexus.subject;

/**
 * @author Xieningjun
 */
public interface SubjectExecutor {

    SubjectHolder sync(Subject subject) throws InterruptedException;

    void async(Subject subject);

    void schedule(Subject subject, long loop, long period);

}
