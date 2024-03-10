package org.nexus.web;

import org.nexus.common.util.PackageScannerUtil;
import org.nexus.common.util.PropertiesUtil;
import org.nexus.web.anno.WebProtocol;
import org.nexus.web.manager.NexusProxyManager;
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

    private static final NexusProxyManager nexusProxyManager = NexusProxyManager.getInstance();

    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        List<Class<?>> clazzList = PackageScannerUtil.scan("org.nexus.web");
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
                        if (nexusProxy instanceof WebProtocolProxy webProtocolProxy) {
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
        /* 从配置文件中读取数据 */
        Properties properties = PropertiesUtil.getProperties("nexus.properties");
        int port = Integer.parseInt((String) properties.get("netty.server.port"));
        /* 启动netty服务端 */
        NettyServer nettyServer = new NettyServer(port);
        nettyServer.start();
        nettyServer.shutdown();
    }

}
