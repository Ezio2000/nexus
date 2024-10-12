package org.nexus.storage.repo;

import org.nexus.subject.Subject;

/**
 * @author Xieningjun
 */
// 用lombok
public class SubjectRepo {

    public String key;

    // todo 封装出去
    // todo 要写builder
    // todo 要支持Repo的子类
    public static SubjectRepo toRepo(Subject subject) {
        return new SubjectRepo() {};
    }

    public static Subject toSubject(SubjectRepo repo) {
        return new Subject() {
            @Override
            public String key() {
                return "";
            }
            @Override
            public Object run0() {
                return "1";
            }
        };
    }

}
