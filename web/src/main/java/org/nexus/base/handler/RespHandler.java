package org.nexus.base.handler;

import io.netty.handler.codec.http.FullHttpResponse;

/**
 * @author Xieningjun
 * @date 2024/3/10 21:17
 * @description
 */
public interface RespHandler {

    Object handle(FullHttpResponse resp) throws Throwable;

}
