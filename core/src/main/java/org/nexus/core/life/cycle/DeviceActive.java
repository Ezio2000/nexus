package org.nexus.core.life.cycle;

import org.nexus.core.life.DeviceLifecycle;
import org.nexus.core.life.DeviceLifecycleContext;
import org.nexus.core.life.LifecycleEnum;
import org.nexus.core.life.resour.Activity;


/**
 * @author Xieningjun
 * @date 2024/8/13 13:41
 * @description
 */
public class DeviceActive extends DeviceLifecycle implements Activity {

    private DeviceLifecycleContext context;

    public DeviceActive(DeviceLifecycleContext context) {
        super(context);
        this.context = context;
    }

    @Override
    public void disconnect0() {
        context.setState(LifecycleEnum.STATE.WAITED);
    }

}
