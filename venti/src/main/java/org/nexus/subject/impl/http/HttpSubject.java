package org.nexus.subject.impl.http;

import org.nexus.subject.Subject;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @author Xieningjun
 */
public class HttpSubject extends Subject {

    private final String key;

    private final HttpClient client;

    private final HttpRequest req;

    private final HttpResponse.BodyHandler<?> resHandler;

    public HttpSubject(String key, HttpClient client, HttpRequest req, HttpResponse.BodyHandler<?> resHandler) {
        this.key = key;
        this.client = client;
        this.req = req;
        this.resHandler = resHandler;
    }

    @Override
    public String key() {
        return "http.%s".formatted(key);
    }

    @Override
    public Object run0() throws Throwable {
        return client.send(req, resHandler);
    }

    @Override
    public void afterResult(Object result) {
        super.afterResult(result);
    }

    @Override
    public void afterError(Throwable t) {
        super.afterError(t);
    }

}
