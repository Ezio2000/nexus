package org.nexus.storage;

import org.nexus.storage.repo.SubjectRepo;
import org.nexus.subject.Subject;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author Xieningjun
 * @date 2024/10/8 17:26
 */
public class SubjectStore {

    private final Map<String, Subject> subjectMap = new ConcurrentHashMap<>();

    private final Exchanger<Collection<SubjectRepo>> exchanger;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1, Thread.ofVirtual().factory());

    public SubjectStore(Exchanger<Collection<SubjectRepo>> exchanger) {
        this.exchanger = exchanger;
    }

    public void flow(boolean in, boolean out) {
        if (in) scheduler.scheduleWithFixedDelay(this::inflow, 0, 10000, TimeUnit.MILLISECONDS);
        if (out) scheduler.scheduleWithFixedDelay(this::outflow, 0, 10000, TimeUnit.MILLISECONDS);
    }

    private void inflow() {
        // todo 怎么把try-catch写得好看
        try {
            var repoCollection = exchanger.inflow();
            for (var repo : repoCollection) {
                subjectMap.putIfAbsent(repo.key, SubjectRepo.toSubject(repo));
            }
        } catch (Throwable t) {
            // todo 完善
            t.printStackTrace();
        }
    }

    private void outflow() {
        try {
            var repoCollection = subjectMap.values().stream().map(SubjectRepo::toRepo).toList();
            exchanger.outflow(repoCollection);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
