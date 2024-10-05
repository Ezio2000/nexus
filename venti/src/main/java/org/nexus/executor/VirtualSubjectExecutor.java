package org.nexus.executor;

import org.nexus.subject.Subject;
import org.nexus.subject.SubjectExecutor;
import org.nexus.subject.SubjectHolder;
import org.nexus.spy.Spy;

/**
 * @author Xieningjun
 */
public class VirtualSubjectExecutor implements SubjectExecutor {

    // todo 变成单例？
    private final Spy spy = new Spy();

    @Override
    public SubjectHolder sync(Subject subject) throws InterruptedException {
        subject.before();
        var thread = spy.one(subject.key(), subject);
        thread.join();
        Thread.ofVirtual().start(subject::after);
        return subject.holder();
    }

    @Override
    public void async(Subject subject) {
        subject.before();
        var thread = spy.one(subject.key(), subject);
        spy.hang(thread, subject::after);
    }

    @Override
    public void schedule(Subject subject, /* 执行次数 */ long loop, /* 执行间隔 */ long period) {
        subject.before();
        var future = spy.loop(subject, subject.key(), loop, period);
        spy.hang(future, subject::after);
    }

    // todo 加一个只找当前的subject的
    public String scrape() {
        return spy.scrape();
    }

}
