package org.nexus.base.client;

import com.google.gson.Gson;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import org.nexus.base.factory.SingletonFactory;
import org.nexus.base.future.AsyncFuture;
import org.nexus.base.future.Future;
import org.nexus.base.future.SyncFuture;
import org.nexus.base.future.FutureManager;

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
        this.group = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
    }

    public void start() {
        this.bootstrap.group(this.group)
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
        this.group.shutdownGracefully();
    }

    public <T> void async(String path, HttpHeaders header, Object reqBody) throws InterruptedException {
        // todo 检查reqBody是否为resp的类型？或者页不需要？
        Future<T> future = this.futureManager.create(AsyncFuture.class);
        this.request(path, header, reqBody, future.getTrace());
    }

    public <T> T sync(String path, HttpHeaders header, Object reqBody) throws InterruptedException {
        // todo 检查reqBody是否为resp的类型？或者页不需要？
        Future<T> future = this.futureManager.create(SyncFuture.class);
        this.request(path, header, reqBody, future.getTrace());
        future.await();
        return future.getResp();
    }

    private void request(String path, HttpHeaders header, Object reqBody, String trace) throws InterruptedException {
        // 加请求参数校验，加在哪比较好？
        ChannelFuture future = this.bootstrap.connect(this.host, this.port).sync();
        DefaultFullHttpRequest req = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, path);
        req.headers().set(HttpHeaderNames.HOST, this.host);
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
