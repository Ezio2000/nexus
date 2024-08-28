package org.nexus.core.life;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.nexus.core.life.cycle.*;
import org.nexus.core.life.resour.Releaser;
import org.nexus.web.client.Contactor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Xieningjun
 * @date 2024/8/13 13:16
 * @description
 */
@Data
@Slf4j
public class DeviceLifecycleContext implements LifecycleContext {

    protected Map<Object, Object> repos = new HashMap<>();

    // ------ 生命周期状态 ------
    protected LifecycleEnum.STATE state = LifecycleEnum.STATE.INITIAL;

    protected Lifecycle initial = new DeviceInitial(this);

    protected Lifecycle preConnected = new DevicePreConnect(this);

    protected Lifecycle active = new DeviceActive(this);

    protected Lifecycle waited = new DeviceWaited(this);

    protected Lifecycle released = new DeviceReleased(this);

    protected Lifecycle cur = initial;

    // todo 要有refresh方法，因为会换
    protected Map<LifecycleEnum.STATE, Lifecycle> lifecycles = new HashMap<>() {
        { put(LifecycleEnum.STATE.INITIAL, initial); }
        { put(LifecycleEnum.STATE.PRE_CONNECTED, preConnected); }
        { put(LifecycleEnum.STATE.ACTIVE, active); }
        { put(LifecycleEnum.STATE.WAITED, waited); }
        { put(LifecycleEnum.STATE.DESTROYED, released); }
    };
    // ------

    // todo 需要初始化
    // ------ 远程客户端 ------
    protected Contactor contactor;

    protected final AtomicBoolean contactStatus = new AtomicBoolean(false);

    /* 探活线程 */
    protected ScheduledExecutorService probeExecutor = Executors.newScheduledThreadPool(1);
    // ------

    @Override
    public void renewCur() {
        cur = lifecycles.get(state);
    }

    @Override
    public void addRepo(Object key, Object value) {
        repos.put(key, value);
    }

    @Override
    public Object getRepo(Object key) {
        return repos.get(key);
    }

    @Override
    public void release() {
        contactor.shutdown();
        probeExecutor.shutdown();
        for (Lifecycle lifecycle : lifecycles.values()) {
            if (lifecycle instanceof Releaser) {
                ((Releaser) lifecycle).release();
            }
        }
    }

}
