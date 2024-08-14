package org.nexus.core.life.cycle;

import org.nexus.core.life.DeviceLifecycle;
import org.nexus.core.life.DeviceLifecycleContext;


/**
 * @author Xieningjun
 * @date 2024/8/13 13:41
 * @description
 */
public class DeviceDestroyed extends DeviceLifecycle {

    public DeviceDestroyed(DeviceLifecycleContext context) {
        super(context);
    }

    @Override
    public void destroy0() {
        System.out.println("已销毁");
    }

}
