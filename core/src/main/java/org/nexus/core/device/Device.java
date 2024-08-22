package org.nexus.core.device;

import org.nexus.core.life.Contactor;
import org.nexus.core.life.DeviceLifecycleContext;
import org.nexus.core.life.Lifecycle;
import org.nexus.core.life.LifecycleEnum;
import org.nexus.core.life.resour.Activity;
import org.nexus.core.life.task.Task;
import org.nexus.core.life.task.TaskManager;

/**
 * @author Xieningjun
 * @date 2024/7/10 13:37
 * @description
 */
public class Device implements Lifecycle {

    protected String host;

    protected int port;

    protected DeviceLifecycleContext context = new DeviceLifecycleContext();

    public Device(String host, int port, Contactor contactor) {
        this.host = host;
        this.port = port;
        context.setContactor(contactor);
        context.addRepo("host", host);
        context.addRepo("port", port);
    }

    @Override
    public void bootstrap() {
        context.getCur().bootstrap();
    }

    @Override
    public void connect() {
        context.getCur().connect();
    }

    @Override
    public void disconnect() {
        context.getCur().disconnect();
    }

    @Override
    public void destroy() {
        context.getCur().destroy();
    }

    public void accept(Task task) {
        if (context.getState() == LifecycleEnum.STATE.ACTIVE) {
            ((Activity) context.getCur()).accept(task);
        }
    }

}
