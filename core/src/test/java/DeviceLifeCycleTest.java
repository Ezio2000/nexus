import lombok.extern.slf4j.Slf4j;
import org.nexus.core.life.Contactor;
import org.nexus.core.life.DeviceLifecycleContext;
import org.nexus.core.life.cycle.*;

/**
 * @author Xieningjun
 * @date 2024/8/13 15:16
 */
@Slf4j
public class DeviceLifeCycleTest {

    public static void main(String[] args) throws InterruptedException {
        DeviceLifecycleContext context = getDeviceLifeCycleContext();

//        System.out.println(context.getCur());
        context.getCur().bootstrap();
//        System.out.println(context.getCur());
        context.getCur().connect();
//        System.out.println(context.getCur());
//        Thread.sleep(2000);
//        context.getCur().active();
//        System.out.println(context.getCur());
//        context.getCur().disconnect();
//        System.out.println(context.getCur());
//        context.getCur().connect();
//        System.out.println(context.getCur());
//        context.getCur().active();
//        System.out.println(context.getCur());
//        context.getCur().destroy();
//        System.out.println(context.getCur());
    }

    private static DeviceLifecycleContext getDeviceLifeCycleContext() {
        DeviceLifecycleContext context = new DeviceLifecycleContext();
        context.setContactor(new Contactor() {
            @Override
            public void bootstrap(String host, int port) {}
            @Override
            public void start() {}
            @Override
            public void shutdown() {}
            @Override
            public boolean probe() {
                log.info("探针激活");
                return true;
            }
        });
        context.addRepo("host", "nexus.com");
        context.addRepo("port", 123);
        return context;
    }

}