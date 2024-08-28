package org.nexus.web.client;

import io.netty.handler.codec.http.HttpHeaders;

/**
 * @author Xieningjun
 * @date 2024/8/13 14:16
 * @description
 */
public interface Contactor {

    void bootstrap(String host, int port);

    void start();

    void shutdown();

    boolean probe();

    Object command(String path, HttpHeaders header, Object reqBody) throws Throwable;

}
