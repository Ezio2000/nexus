package org.nexus.core.life;

import org.nexus.core.life.ex.LifecycleException;
import org.nexus.core.life.resour.Releaser;

/**
 * @author Xieningjun
 * @date 2024/8/12 11:45
 * @description Nexus设备的生命周期
 */
public abstract class DeviceLifecycle implements Lifecycle {
    
    protected final LifecycleContext context;
    
    public DeviceLifecycle(LifecycleContext context) {
        this.context = context;
    }

    @Override
    public void bootstrap() {
        try {
            bootstrap0();
        } catch (LifecycleException e) {
            context.setState(e.state);
        }
        context.renewCur();
    }

    @Override
    public void connect() {
        try {
            connect0();
        } catch (LifecycleException e) {
            context.setState(e.state);
        }
        context.renewCur();
    }

    @Override
    public void disconnect() {
        try {
            disconnect0();
        } catch (LifecycleException e) {
            context.setState(e.state);
        }
        context.renewCur();
    }

    @Override
    public void destroy() {
        destroy0();
        context.setState(LifecycleEnum.STATE.DESTROYED);
        context.renewCur();
        context.release();
    }

    /**
     * 重写下面这些方法时，需调用context.setState(State state)，方可让状态机扭转状态
     */
    public void bootstrap0() throws LifecycleException {}

    public void connect0() throws LifecycleException {}

    public void disconnect0() throws LifecycleException {}

    public void destroy0() {}

}
