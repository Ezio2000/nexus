package org.nexus.core.life.cycle;

import lombok.extern.slf4j.Slf4j;
import org.nexus.core.life.Contactor;
import org.nexus.core.life.DeviceLifecycle;
import org.nexus.core.life.DeviceLifecycleContext;
import org.nexus.core.life.LifecycleEnum;


/**
 * @author Xieningjun
 * @date 2024/8/13 13:41
 * @description
 */
@Slf4j
public class DeviceInitial extends DeviceLifecycle {

    /* 代替父类中的context */
    protected DeviceLifecycleContext context;

    public DeviceInitial(DeviceLifecycleContext context) {
        super(context);
        this.context = context;
    }

    @Override
    public void bootstrap0() {
        String host = (String) context.getRepo("host");
        int port = (int) context.getRepo("port");
        Contactor contactor = context.getContactor();
        contactor.bootstrap(host, port);
        context.setState(LifecycleEnum.STATE.PRE_CONNECTED);
    }

}
