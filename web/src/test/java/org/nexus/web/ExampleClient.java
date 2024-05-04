package org.nexus.web;

import org.nexus.web.factory.SingletonFactory;
import org.nexus.web.util.PackageScannerUtil;
import org.nexus.web.util.PropertiesUtil;
import org.nexus.web.anno.WebProtocol;
import org.nexus.web.client.NettyClient;
import org.nexus.web.manager.WebNexusProxyManager;
import org.nexus.web.pojo.NexusProxy;
import org.nexus.web.pojo.WebProtocolProxy;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Properties;

/**
 * @author Xieningjun
 * @date 2024/3/10 18:06
 * @description
 */
public class ExampleClient {

    private static final WebNexusProxyManager WEB_NEXUS_PROXY_MANAGER = SingletonFactory.factory().generate(
            WebNexusProxyManager.class, WebNexusProxyManager::new
    );

    public static void main(String[] args) throws Exception {
        WebNexusProxyLoader.loadWebNexusProxy();
        /* 从配置文件中读取数据 */
        Properties properties = PropertiesUtil.getProperties("nexus.properties");
        String host = (String) properties.get("netty.client.host");
        int port = Integer.parseInt((String) properties.get("netty.client.port"));
        /* 启动netty客户端 */
        NettyClient nettyClient = new NettyClient(host, port);
        nettyClient.start();
        /* 发送请求 */
        ExampleWebProtocolProxy.ExampleReq req = new ExampleWebProtocolProxy.ExampleReq();
        req.reqCode = 1;
        req.str = "我讨厌慧芳";
        nettyClient.async("/example", null, req);
        nettyClient.async("/error", null, req);
        ExampleWebProtocolProxy.ExampleResp resp = nettyClient.sync("/example", null, req);
        System.out.println(resp.respCode);
        System.out.println(resp.str);
        System.out.println(resp.lists);
        nettyClient.shutdown();
    }

}
