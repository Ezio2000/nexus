package org.nexus.storage;

import org.nexus.subject.Subject;

import java.util.Map;

/**
 * @author Xieningjun
 * @date 2024/10/8 17:26
 */
public class SubjectStore {

    private Map<String, Subject> subjectMap;

    private Exchanger<Subject> exchanger;

    public void inflow() {
        var subject = exchanger.inflow();
//        subjectMap.put(subject.key(), subject);
    }

    public void outflow() {
        for (var subject : subjectMap.values()) {
//            exchanger.outflow(subject);
        }
    }

}
