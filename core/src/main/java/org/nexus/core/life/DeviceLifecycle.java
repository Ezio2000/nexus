package org.nexus.core.life;

import org.nexus.core.life.ex.LifecycleException;

/**
 * @author Xieningjun
 * @date 2024/8/12 11:45
 * @description Nexus设备的生命周期
 */
public abstract class DeviceLifecycle implements Lifecycle {
    
    protected final DeviceLifecycleContext context;
    
    public DeviceLifecycle(DeviceLifecycleContext context) {
        this.context = context;
    }

    public void bootstrap() {
        try {
            bootstrap0();
        } catch (LifecycleException ex) {
            context.setState(ex.state);
        }
        context.renewCur();
    }

    public void connect() {
        try {
            connect0();
        } catch (LifecycleException ex) {
            context.setState(ex.state);
        }
        context.renewCur();
    }

    public void active() {
        try {
            active0();
        } catch (LifecycleException ex) {
            context.setState(ex.state);
        }
        context.renewCur();
    }

    public void disconnect() {
        try {
            disconnect0();
        } catch (LifecycleException ex) {
            context.setState(ex.state);
        }
        context.renewCur();
    }

    public void destroy() {
        destroy0();
        context.renewCur();
    }

    /**
     * 重写下面这些方法时，需调用context.setState(State state)，方可让状态机扭转状态
     */
    public void bootstrap0() throws LifecycleException {}

    public void connect0() throws LifecycleException {}

    public void active0() throws LifecycleException {}

    public void disconnect0() throws LifecycleException {}

    public void destroy0() {}

}
