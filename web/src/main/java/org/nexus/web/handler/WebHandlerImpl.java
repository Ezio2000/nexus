package org.nexus.web.handler;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import org.nexus.common.ex.NexusException;
import org.nexus.web.manager.NexusProxyManager;
import org.nexus.web.pojo.NexusProxy;
import org.nexus.web.pojo.WebProtocolProxy;

import java.util.Objects;

/**
 * @author Xieningjun
 * @date 2024/3/10 21:32
 * @description
 */
public class WebHandlerImpl implements ReqHandler, RespHandler {

    private final NexusProxyManager nexusProxyManager = NexusProxyManager.getInstance();

    @Override
    public Object handle(FullHttpRequest req) throws Throwable {
        String uri = req.uri().split("\\?")[0];
        for (NexusProxy nexusProxy : nexusProxyManager.values()) {
            if (nexusProxy instanceof WebProtocolProxy) {
                WebProtocolProxy webProtocolProxy = (WebProtocolProxy) nexusProxy;
                if (
                        Objects.equals(webProtocolProxy.getUri(), uri)
                                && webProtocolProxy.getReqHandler() != null
                ) {
                    /* business code */
                    return ((WebProtocolProxy) nexusProxy).handleReq(req);
                }
            }
        }
        throw new NexusException("Find no proxy for uri " + uri + ".");
    }

    @Override
    public Object handle(FullHttpResponse resp) throws Throwable {
        String uri = resp.headers().get("uri");
        for (NexusProxy nexusProxy : nexusProxyManager.values()) {
            if (nexusProxy instanceof WebProtocolProxy) {
                WebProtocolProxy webProtocolProxy = (WebProtocolProxy) nexusProxy;
                if (
                        Objects.equals(webProtocolProxy.getUri(), uri)
                                && webProtocolProxy.getRespHandler() != null
                ) {
                    /* business code */
                    return ((WebProtocolProxy) nexusProxy).handleResp(resp);
                }
            }
        }
        throw new NexusException("Find no proxy for uri " + uri + ".");
    }

}
