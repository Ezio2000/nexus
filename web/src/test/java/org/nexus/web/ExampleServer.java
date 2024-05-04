package org.nexus.web;

import org.nexus.web.factory.SingletonFactory;
import org.nexus.web.util.PackageScannerUtil;
import org.nexus.web.util.PropertiesUtil;
import org.nexus.web.anno.WebProtocol;
import org.nexus.web.manager.WebNexusProxyManager;
import org.nexus.web.pojo.NexusProxy;
import org.nexus.web.pojo.WebProtocolProxy;
import org.nexus.web.server.NettyServer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
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
