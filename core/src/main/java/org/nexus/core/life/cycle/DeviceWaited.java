package org.nexus.core.life.cycle;

import org.nexus.core.life.DeviceLifecycle;
import org.nexus.core.life.DeviceLifecycleContext;
import org.nexus.core.life.LifecycleEnum;


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
        context.setState(LifecycleEnum.STATE.ACTIVE);
    }

}