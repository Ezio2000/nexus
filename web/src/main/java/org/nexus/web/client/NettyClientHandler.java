package org.nexus.web.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.nexus.web.handler.RespHandler;
import org.nexus.web.future.FutureManager;
import org.nexus.web.handler.WebHandlerImpl;

/**
 * @author Xieningjun
 * @date 2024/2/27 10:54
 * @description Netty客户端处理器
 */
// TODO: 2024/3/11 超时和异常时主动关闭连接
@Slf4j
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private final RespHandler respHandler = new WebHandlerImpl();

    private final FutureManager futureManager = FutureManager.getInstance();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof FullHttpResponse resp) {
            Object handledResp = null;
            try {
                // TODO: 2024/3/9
                handledResp = respHandler.handle(resp);
            } catch (Throwable t) {
                log.error("", t);
                throw new RuntimeException(t);
            } finally {
                String trace = resp.headers().get("trace");
                this.futureManager.finish(trace, handledResp);
                resp.release();
                ctx.close();
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        // 连接关闭时打印日志
        log.warn("Connection closed.");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("Channel handle error.", cause);
        ctx.close();
    }

}
