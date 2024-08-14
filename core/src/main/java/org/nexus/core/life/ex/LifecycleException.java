package org.nexus.core.life.ex;

import org.nexus.core.life.LifecycleEnum;

/**
 * @author Xieningjun
 * @date 2024/8/13 19:50
 * @description
 */
public class LifecycleException extends Exception {

    public LifecycleEnum.STATE state;

    public LifecycleException(LifecycleEnum.STATE state) {
        this.state = state;
    }

}
