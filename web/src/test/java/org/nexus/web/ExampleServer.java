package org.nexus.web;

import org.nexus.base.factory.SingletonFactory;
import org.nexus.base.util.PropertiesUtil;
import org.nexus.web.manager.WebNexusProxyManager;
import org.nexus.web.server.NettyServer;

import java.util.Properties;

/**
 * @author Xieningjun
 * @date 2024/3/9 16:11
 * @description
 */
public class ExampleServer {

    private static final WebNexusProxyManager WEB_NEXUS_PROXY_MANAGER = SingletonFactory.factory().generate(
            WebNexusProxyManager.class, WebNexusProxyManager::new
    );

    public static void main(String[] args) throws Exception {
        WebNexusProxyLoader.loadWebNexusProxy();
        /* 从配置文件中读取数据 */
        Properties properties = PropertiesUtil.getProperties("nexus.properties");
        int port = Integer.parseInt((String) properties.get("netty.server.port"));
        /* 启动netty服务端 */
        NettyServer nettyServer = new NettyServer(port);
        nettyServer.start();
        nettyServer.shutdown();
    }

}
