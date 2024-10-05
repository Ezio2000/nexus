package org.nexus.subject;

/**
 * @author Xieningjun
 */
public interface SubjectRunnable extends Runnable {

    String key();

    void before();

    void after();

    SubjectState state();

}
