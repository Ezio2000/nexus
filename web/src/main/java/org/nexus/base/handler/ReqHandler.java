package org.nexus.base.handler;

import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @author Xieningjun
 * @date 2024/3/10 21:17
 * @description
 */
public interface ReqHandler {

    Object handle(FullHttpRequest req) throws Throwable;

}
