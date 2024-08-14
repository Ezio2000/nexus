package org.nexus.core.life.cycle;

import org.nexus.core.life.DeviceLifecycle;
import org.nexus.core.life.DeviceLifecycleContext;


/**
 * @author Xieningjun
 * @date 2024/8/13 13:41
 * @description
 */
public class DeviceWaited extends DeviceLifecycle {

    public DeviceWaited(DeviceLifecycleContext context) {
        super(context);
    }

    @Override
    public void connect0() {
        System.out.println("重连");
    }

    @Override
    public void destroy0() {
        System.out.println("销毁");
    }

}
