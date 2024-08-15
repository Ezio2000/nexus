package org.nexus.core.life;

import org.nexus.core.life.resour.Releaser;

/**
 * @author Xieningjun
 * @date 2024/8/13 15:28
 * @description
 */
public interface LifecycleContext extends Releaser {

    void setState(LifecycleEnum.STATE state);

    void renewCur();

    void addRepo(Object key, Object value);

    Object getRepo(Object key);

    void release();

}
