package org.nexus.storage;

import com.google.gson.Gson;
import org.nexus.http.GenericBodyHandler;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @author Xieningjun
 */
public abstract class HttpExchanger<T> implements Exchanger<T> {

    // 要加状态

    private final Type type;

    private final HttpClient client;

    public HttpExchanger(Type type, HttpClient httpClient) {
        this.type = type;
        this.client = httpClient;
    }

    public abstract HttpRequest inflowReq();

    public abstract HttpRequest outflowReq(T t);

    @Override
    public final T inflow() throws IOException, InterruptedException {
        var inflowReq = inflowReq();
        HttpResponse<T> res = client.send(inflowReq, new GenericBodyHandler<>(type));
        return res.body();
    }

    @Override
    public final void outflow(T t) throws IOException, InterruptedException {
        var outflowReq = outflowReq(t);
        client.send(outflowReq, HttpResponse.BodyHandlers.discarding());
    }

}
