package org.nexus.web.client;

import com.google.gson.Gson;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import org.nexus.base.factory.SingletonFactory;
import org.nexus.web.future.AsyncFuture;
import org.nexus.web.future.Future;
import org.nexus.web.future.SyncFuture;
import org.nexus.web.future.FutureManager;

import java.nio.charset.StandardCharsets;

/**
 * @author Xieningjun
 * @date 2024/2/27 10:55
 * @description Netty客户端
 */
public class NettyClient {

    private final String host;

    private final int port;

    private final EventLoopGroup group;

    private final Bootstrap bootstrap;

    private final FutureManager futureManager = SingletonFactory.factory().generate(
            FutureManager.class, FutureManager::new
    );

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
    }

    public void start() {
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new HttpClientCodec());
                        pipeline.addLast(new HttpObjectAggregator(65536));
                        pipeline.addLast(new NettyClientHandler());
                    }
                });
    }

    public void shutdown() {
        group.shutdownGracefully();
    }

    public <T> void async(String path, HttpHeaders header, Object reqBody) throws InterruptedException {
        // todo 检查reqBody是否为resp的类型？或者页不需要？
        Future<T> future = futureManager.create(AsyncFuture.class);
        request(path, header, reqBody, future.getTrace());
    }

    public <T> T sync(String path, HttpHeaders header, Object reqBody) throws Throwable {
        // todo 检查reqBody是否为resp的类型？或者页不需要？
        Future<T> future = futureManager.create(SyncFuture.class);
        request(path, header, reqBody, future.getTrace());
        future.await();
        // todo 判断这个resp是否为ex
        if (!future.isThrowable()) {
            return future.getResp();
        } else {
            throw (Throwable) future.getResp();
        }
    }

    private void request(String path, HttpHeaders header, Object reqBody, String trace) throws InterruptedException {
        // 加请求参数校验，加在哪比较好？
        ChannelFuture future = bootstrap.connect(host, port).sync();
        DefaultFullHttpRequest req = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, path);
        req.headers().set(HttpHeaderNames.HOST, host);
        req.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
        req.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
        req.headers().set("trace", trace);
        if (header != null) {
            req.headers().add(header);
        }
        if (reqBody != null) {
            String bodyJson = new Gson().toJson(reqBody);
            byte[] bodyBytes = bodyJson.getBytes(StandardCharsets.UTF_8);
            req.headers().set(HttpHeaderNames.CONTENT_LENGTH, bodyBytes.length);
            req.content().writeBytes(bodyBytes);
        }
        future.channel().writeAndFlush(req);
        future.channel().closeFuture().sync();
    }

}
