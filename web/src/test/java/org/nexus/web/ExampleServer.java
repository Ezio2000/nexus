package org.nexus.web;

import org.nexus.common.pack.PackageScanner;
import org.nexus.web.anno.WebProtocol;
import org.nexus.web.manager.NexusProxyManager;
import org.nexus.web.pojo.NexusProxy;
import org.nexus.web.pojo.WebProtocolProxy;
import org.nexus.web.server.NettyServer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Xieningjun
 * @date 2024/3/9 16:11
 * @description
 */
public class ExampleServer {

    private static final NexusProxyManager nexusProxyManager = NexusProxyManager.getInstance();

    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        List<Class<?>> clazzList = PackageScanner.scan("org.nexus.web");
        for (Class<?> clazz : clazzList) {
            WebProtocol annotation = clazz.getAnnotation(WebProtocol.class);
            if (annotation != null) {
                Object instance = clazz.getConstructor().newInstance();
                for (Method method : clazz.getMethods()) {
                    WebProtocol.Server server = method.getAnnotation(WebProtocol.Server.class);
                    if (server != null && server.role() == WebProtocol.Role.SERVER) {
                        NexusProxy nexusProxy = nexusProxyManager.containsKey(annotation.nexus().name())
                                ? nexusProxyManager.get(annotation.nexus().name())
                                : new WebProtocolProxy();
                        if (nexusProxy instanceof WebProtocolProxy) {
                            WebProtocolProxy webProtocolProxy = (WebProtocolProxy) nexusProxy;
                            webProtocolProxy.setName(annotation.nexus().name());
                            webProtocolProxy.setClazz(clazz);
                            webProtocolProxy.setInstance(instance);
                            webProtocolProxy.setUri(server.uri());
                            webProtocolProxy.setReqHandler(method);
                            nexusProxyManager.put(annotation.nexus().name(), webProtocolProxy);
                        }
                    }
                }
            }
        }
        NettyServer nettyServer = new NettyServer(8002);
        nettyServer.start();
        nettyServer.shutdown();
    }

}
