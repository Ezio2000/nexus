package org.nexus.web;

import org.nexus.base.factory.SingletonFactory;
import org.nexus.base.util.PropertiesUtil;
import org.nexus.web.client.NettyClient;
import org.nexus.web.manager.WebNexusProxyManager;

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
