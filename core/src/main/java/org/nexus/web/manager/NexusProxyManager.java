package org.nexus.web.manager;

import org.nexus.web.pojo.NexusProxy;

import java.util.HashMap;

/**
 * @author Xieningjun
 * @date 2024/2/28 1:26
 * @description
 */
public class NexusProxyManager extends HashMap<String, NexusProxy> {

    /**
     * 唯一的实例
     */
    private volatile static NexusProxyManager INSTANCE;

    /**
     * 私有化构造方法，防止外部实例化
     */
    private NexusProxyManager() {
    }

    /**
     * 获取唯一的实例
     * @return RmqApiManager 实例
     */
    public static NexusProxyManager getInstance() {
        if (null == INSTANCE) {
            synchronized (NexusProxyManager.class) {
                if (null == INSTANCE) {
                    INSTANCE = new NexusProxyManager();
                }
            }
        }
        return INSTANCE;
    }

}
