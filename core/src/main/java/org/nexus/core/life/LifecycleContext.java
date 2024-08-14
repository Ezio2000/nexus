package org.nexus.core.life;

/**
 * @author Xieningjun
 * @date 2024/8/13 15:28
 * @description
 */
public interface LifecycleContext {

    void addRepo(Object key, Object value);

    Object getRepo(Object key);

}
