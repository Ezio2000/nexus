package org.nexus.subject.impl;

import org.nexus.subject.Subject;
import org.nexus.subject.impl.http.HttpSubject;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @author Xieningjun
 */
public class SubjectFactory {
    
    public static HttpSubjectBuilder ofHttpSubject() {
        return new HttpSubjectBuilder();
    }

    public static class HttpSubjectBuilder {
        private String key;
        private HttpClient client;
        private HttpRequest req;
        private HttpResponse.BodyHandler<?> resHandler;
        public HttpSubjectBuilder key(String key) {
            this.key = key;
            return this;
        }
        public HttpSubjectBuilder client(HttpClient client) {
            this.client = client;
            return this;
        }
        public HttpSubjectBuilder req(HttpRequest req) {
            this.req = req;
            return this;
        }
        public HttpSubjectBuilder resHandler(HttpResponse.BodyHandler<?> resHandler) {
            this.resHandler = resHandler;
            return this;
        }
        public Subject build() {
            return new HttpSubject(key, client, req, resHandler);
        }
    }
    
}
