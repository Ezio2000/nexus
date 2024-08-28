package org.nexus.core.life.cycle;

import org.nexus.core.life.DeviceLifecycle;
import org.nexus.core.life.DeviceLifecycleContext;
import org.nexus.core.life.LifecycleEnum;
import org.nexus.core.life.resour.Activity;
import org.nexus.core.life.resour.Releaser;
import org.nexus.core.life.task.Task;
import org.nexus.core.life.task.TaskManager;


/**
 * @author Xieningjun
 * @date 2024/8/13 13:41
 * @description
 */
public class DeviceActive extends DeviceLifecycle implements Activity, Releaser {

    protected DeviceLifecycleContext context;

    private final TaskManager taskManager = new TaskManager();

    public DeviceActive(DeviceLifecycleContext context) {
        super(context);
        this.context = context;
    }

    @Override
    public void disconnect0() {
        context.setState(LifecycleEnum.STATE.WAITED);
    }

    @Override
    public void accept(Task task) {
        taskManager.accept(task);
    }

    @Override
    public void release() {
        taskManager.release();
    }

}
