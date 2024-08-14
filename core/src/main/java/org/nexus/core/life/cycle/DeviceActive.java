package org.nexus.core.life.cycle;

import org.nexus.core.life.DeviceLifecycle;
import org.nexus.core.life.DeviceLifecycleContext;


/**
 * @author Xieningjun
 * @date 2024/8/13 13:41
 * @description
 */
public class DeviceActive extends DeviceLifecycle {

    public DeviceActive(DeviceLifecycleContext context) {
        super(context);
    }

    @Override
    public void active0() {
        System.out.println("活跃中");
    }

    @Override
    public void disconnect0() {
        System.out.println("断开连接");
    }

}
