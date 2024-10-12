package org.nexus.executor;

import org.nexus.vir.Virtual;
import org.nexus.subject.Subject;
import org.nexus.subject.SubjectExecutor;
import org.nexus.subject.SubjectHolder;

/**
 * @author Xieningjun
 */
public class VirtualSubjectExecutor implements SubjectExecutor {

    @Override
    public SubjectHolder sync(Subject subject) throws InterruptedException {
        subject.before();
        var thread = Virtual.spy(subject, subject.key());
        thread.join();
        Thread.ofVirtual().start(subject::after);
        return subject.holder();
    }

    @Override
    public void async(Subject subject) {
        subject.before();
        var thread = Virtual.spy(subject, subject.key());
        Virtual.hang(thread, subject::after);
    }

    @Override
    public void schedule(Subject subject, /* 执行次数 */ long loop, /* 执行间隔 */ long period) {
        subject.before();
        var future = Virtual.spy(subject, subject.key(), loop, period);
        Virtual.hang(future, subject::after);
    }

    // todo 加一个只找当前的subject的
    public String scrape() {
        return Virtual.scrape();
    }

}
