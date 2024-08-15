package org.nexus.core.life.cycle;

import lombok.extern.slf4j.Slf4j;
import org.nexus.core.life.DeviceLifecycle;
import org.nexus.core.life.DeviceLifecycleContext;
import org.nexus.core.life.LifecycleEnum;
import org.nexus.core.life.ex.LifecycleException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


/**
 * @author Xieningjun
 * @date 2024/8/13 13:41
 * @description
 */
@Slf4j
public class DevicePreConnect extends DeviceLifecycle {

    protected DeviceLifecycleContext context;

    public DevicePreConnect(DeviceLifecycleContext context) {
        super(context);
        this.context = context;
    }

    @Override
    public void connect0() throws LifecycleException {
        context.getContactor().start();
        CountDownLatch latch = new CountDownLatch(1);
        Runnable runnable = () -> {
            context.getContactStatus().set(context.getContactor().probe());
            log.debug("Contactor probe: {}", context.getContactStatus());
            latch.countDown();
        };
        context.getProbeExecutor().scheduleAtFixedRate(runnable, 0, 1, TimeUnit.SECONDS);
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (context.getContactStatus().get()) {
            context.setState(LifecycleEnum.STATE.ACTIVE);
        } else {
            // todo 改成host/port的提示
            log.error("[{}] connect fail.", context.getContactor());
            throw new LifecycleException(LifecycleEnum.STATE.DESTROYED);
        }
    }

}
