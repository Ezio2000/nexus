package org.nexus.storage;

import org.nexus.http.GenericBodyHandler;
import org.nexus.subject.Subject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @author Xieningjun
 */
public class HttpExchanger<T> implements Exchanger<T> {

    // 要加状态

    private Type type;

    private HttpClient client;

    private HttpRequest req;

    public HttpExchanger(Type type, HttpClient httpClient, HttpRequest req) {
        this.type = type;
        this.client = httpClient;
        this.req = req;
    }

    @Override
    public T inflow() {
        try {
            HttpResponse<T> res = client.send(req, new GenericBodyHandler<>(type));
            return res.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void outflow(T t) {

    }

}
