package org.nexus.web;

import org.nexus.web.anno.WebProtocol;
import org.nexus.web.factory.SingletonFactory;
import org.nexus.web.manager.WebNexusProxyManager;
import org.nexus.web.pojo.WebProtocolProxy;
import org.nexus.web.util.PackageScannerUtil;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Xieningjun
 * @date 2024/5/4 17:43
 * @description
 */
public class WebNexusProxyLoader {

    private static final WebNexusProxyManager WEB_NEXUS_PROXY_MANAGER = SingletonFactory.factory().generate(
            WebNexusProxyManager.class, WebNexusProxyManager::new
    );

    public static void loadWebNexusProxy() throws Exception {
        List<Class<?>> clazzList = PackageScannerUtil.scan("org.nexus.web");
        for (Class<?> clazz : clazzList) {
            WebProtocol annotation = clazz.getAnnotation(WebProtocol.class);
            if (annotation != null) {
                Object instance = clazz.getConstructor().newInstance();
                WebProtocolProxy webProtocolProxy = WEB_NEXUS_PROXY_MANAGER.register(annotation.name(), new WebProtocolProxy());
                webProtocolProxy.setName(annotation.name());
                webProtocolProxy.setClazz(clazz);
                webProtocolProxy.setInstance(instance);
                webProtocolProxy.setUri(annotation.uri());
                for (Method method : clazz.getMethods()) {
                    /* load SERVER protocol */
                    WebProtocol.Server server = method.getAnnotation(WebProtocol.Server.class);
                    if (server != null && server.role() == WebProtocol.Role.SERVER) {
                        webProtocolProxy.setReqHandler(method);
                        WEB_NEXUS_PROXY_MANAGER.mappingUri(annotation.uri(), annotation.name());
                    }
                    /* load CLIENT protocol */
                    WebProtocol.Client client = method.getAnnotation(WebProtocol.Client.class);
                    if (client != null && client.role() == WebProtocol.Role.CLIENT) {
                        webProtocolProxy.setRespHandler(method);
                        WEB_NEXUS_PROXY_MANAGER.mappingUri(annotation.uri(), annotation.name());
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        loadWebNexusProxy();
        System.out.println(WEB_NEXUS_PROXY_MANAGER);
    }

}
