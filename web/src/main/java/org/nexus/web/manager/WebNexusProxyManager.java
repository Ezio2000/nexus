package org.nexus.web.manager;

import org.nexus.web.pojo.WebProtocolProxy;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Xieningjun
 * @date 2024/2/28 1:26
 * @description
 */
public class WebNexusProxyManager {

    private static final Map<String, WebProtocolProxy> manager = new HashMap<>();

    private static final Map<String/* uri */, String/* anno-name */> uriMapping = new HashMap<>();

    public WebProtocolProxy register(String name, WebProtocolProxy proxy) {
        return manager.computeIfAbsent(name, k -> proxy);
    }

    public String mappingUri(String uri, String name) {
        return uriMapping.computeIfAbsent(uri, k -> name);
    }

    public WebProtocolProxy getByName(String name) {
        return manager.get(name);
    }

    public WebProtocolProxy getByUri(String uri) {
        String name = uriMapping.get(uri);
        return manager.get(name);
    }

}
