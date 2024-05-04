package org.nexus.web.handler;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import org.nexus.web.ex.NexusException;
import org.nexus.web.factory.SingletonFactory;
import org.nexus.web.manager.WebNexusProxyManager;
import org.nexus.web.pojo.WebProtocolProxy;

/**
 * @author Xieningjun
 * @date 2024/3/10 21:32
 * @description
 */
public class WebHandlerImpl implements ReqHandler, RespHandler {

    private static final WebNexusProxyManager WEB_NEXUS_PROXY_MANAGER = SingletonFactory.factory().generate(
            WebNexusProxyManager.class, WebNexusProxyManager::new
    );

    @Override
    public Object handle(FullHttpRequest req) throws Throwable {
        String uri = req.uri().split("\\?")[0];
        WebProtocolProxy webProtocolProxy = WEB_NEXUS_PROXY_MANAGER.getByUri(uri);
        if (webProtocolProxy != null && webProtocolProxy.getReqHandler() != null) {
            return webProtocolProxy.handleReq(req);
        }
        throw new NexusException("Find no proxy for uri " + uri + ".");
    }

    @Override
    public Object handle(FullHttpResponse resp) throws Throwable {
        String uri = resp.headers().get("uri");
        WebProtocolProxy webProtocolProxy = WEB_NEXUS_PROXY_MANAGER.getByUri(uri);
        if (webProtocolProxy != null && webProtocolProxy.getRespHandler() != null) {
            return webProtocolProxy.handleResp(resp);
        }
        throw new NexusException("Find no proxy for uri " + uri + ".");
    }

}
