package org.nexus.web.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;
import org.nexus.web.handler.ReqHandler;
import org.nexus.web.handler.WebHandlerImpl;
import org.nexus.web.util.Transformer;

import java.nio.charset.StandardCharsets;

/**
 * @author Xieningjun
 * @date 2024/2/27 10:17
 * @description 服务端处理器
 */
@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private final ReqHandler reqHandler = new WebHandlerImpl();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof FullHttpRequest req) {
            FullHttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            try {
                String uri = req.uri().split("\\?")[0];
                String trace = req.headers().get("trace");
                resp.headers().set("uri", uri);
                resp.headers().set("trace", trace);

                // TODO: 2024/3/9
                Object respBody = reqHandler.handle(req);

                if (respBody != null) {
                    String bodyJson = Transformer.object2Json(respBody);
                    byte[] bodyBytes = bodyJson.getBytes(StandardCharsets.UTF_8);
                    resp.content().writeBytes(bodyBytes);
                }
                resp.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=UTF-8");
                resp.headers().set(HttpHeaderNames.CONTENT_LENGTH, resp.content().readableBytes());
                ctx.writeAndFlush(resp);
            } catch (Throwable t) {
                log.error("", t);
                throw new RuntimeException(t);
            } finally {
                // TODO: 2024/2/28 release是干嘛的
                req.release();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("Channel handle error.", cause);
        ctx.close();
    }

}
