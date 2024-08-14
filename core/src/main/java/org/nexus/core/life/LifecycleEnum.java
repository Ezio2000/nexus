package org.nexus.core.life;

/**
 * @author Xieningjun
 * @date 2024/8/13 11:40
 * @description 生命周期枚举
 */
public class LifecycleEnum {

    public static enum STATE {
        INITIAL, PRE_CONNECTED, ACTIVE, WAITED, DESTROYED
    }

}
