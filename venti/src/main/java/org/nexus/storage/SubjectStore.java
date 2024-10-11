package org.nexus.storage;

import org.nexus.subject.Subject;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author Xieningjun
 * @date 2024/10/8 17:26
 */
public class SubjectStore {

    private final Map<String, Subject> subjectMap = new ConcurrentHashMap<>();

    private final Exchanger<Collection<Subject>> exchanger;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1, Thread.ofVirtual().factory());

    public SubjectStore(final Exchanger<Collection<Subject>> exchanger) {
        this.exchanger = exchanger;
    }

    public void inflow() {
        // todo 怎么把try-catch写得好看
        try {
            var subjects = exchanger.inflow();
            for (var subject : subjects) {
                subjectMap.putIfAbsent(subject.key(), subject);
            }
        } catch (Throwable t) {
            // todo 完善
            t.printStackTrace();
        }
    }

    public void outflow() {
        try {
            exchanger.outflow(subjectMap.values());
        } catch (Throwable t) {
            // todo 完善
            t.printStackTrace();
        }
    }

}
