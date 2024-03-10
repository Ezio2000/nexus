package org.nexus.web;

import org.nexus.common.pack.PackageScanner;
import org.nexus.web.anno.WebProtocol;
import org.nexus.web.client.NettyClient;
import org.nexus.web.manager.NexusProxyManager;
import org.nexus.web.pojo.NexusProxy;
import org.nexus.web.pojo.WebProtocolProxy;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Xieningjun
 * @date 2024/3/10 18:06
 * @description
 */
public class ExampleClient {

    private static final NexusProxyManager nexusProxyManager = NexusProxyManager.getInstance();

    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        List<Class<?>> clazzList = PackageScanner.scan("org.nexus.web");
        for (Class<?> clazz : clazzList) {
            WebProtocol annotation = clazz.getAnnotation(WebProtocol.class);
            if (annotation != null) {
                Object instance = clazz.getConstructor().newInstance();
                for (Method method : clazz.getMethods()) {
                    WebProtocol.Client client = method.getAnnotation(WebProtocol.Client.class);
                    if (client != null && client.role() == WebProtocol.Role.CLIENT) {
                        NexusProxy nexusProxy = nexusProxyManager.containsKey(annotation.nexus().name())
                                ? nexusProxyManager.get(annotation.nexus().name())
                                : new WebProtocolProxy();
                        if (nexusProxy instanceof WebProtocolProxy) {
                            WebProtocolProxy webProtocolProxy = (WebProtocolProxy) nexusProxy;
                            webProtocolProxy.setName(annotation.nexus().name());
                            webProtocolProxy.setClazz(clazz);
                            webProtocolProxy.setInstance(instance);
                            webProtocolProxy.setUri(client.uri());
                            webProtocolProxy.setRespHandler(method);
                            nexusProxyManager.put(annotation.nexus().name(), webProtocolProxy);
                        }
                    }
                }
            }
        }
        NettyClient nettyClient = new NettyClient("localhost", 8002);
        nettyClient.start();
        ExampleWebProtocolProxy.ExampleReq req = new ExampleWebProtocolProxy.ExampleReq();
        req.reqCode = 1;
        req.str = "慧芳很好看";
        nettyClient.async("/example", null, req);
        ExampleWebProtocolProxy.ExampleResp resp = nettyClient.sync("/example", null, req);
        System.out.println(resp.respCode);
        System.out.println(resp.str);
        System.out.println(resp.lists);
        nettyClient.shutdown();
    }

}
