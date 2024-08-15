package org.nexus.core.life.cycle;

import org.nexus.core.life.DeviceLifecycle;
import org.nexus.core.life.DeviceLifecycleContext;
import org.nexus.core.life.resour.Releaser;


/**
 * @author Xieningjun
 * @date 2024/8/13 13:41
 * @description
 */
public class DeviceReleased extends DeviceLifecycle implements Releaser {

    public DeviceReleased(DeviceLifecycleContext context) {
        super(context);
    }

    @Override
    public void release() {
        System.out.println("释放资源！");
    }

}
