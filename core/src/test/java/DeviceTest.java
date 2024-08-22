import org.nexus.core.device.Device;
import org.nexus.core.life.Contactor;
import org.nexus.core.life.ex.TaskWorkException;
import org.nexus.core.life.task.Task;

/**
 * @author Xieningjun
 * @date 2024/8/22 15:55
 * @description
 */
public class DeviceTest {

    public static void main(String[] args) throws InterruptedException {
        Contactor contactor = new Contactor() {
            @Override
            public void bootstrap(String host, int port) {}
            @Override
            public void start() {}
            @Override
            public void shutdown() {}
            @Override
            public boolean probe() {return true;}
        };
        Device device = new Device("org.nexus.com", 44060, contactor);
        device.bootstrap();
        device.connect();
        Task task = new TestTask();
        device.accept(task);
        task.await();
        System.out.println(task.getFuture().getResp());
//        device.disconnect();
//        device.destroy();
    }

    public static class TestTask extends Task {
        @Override
        protected Object doTask() throws TaskWorkException {
            try {
                System.out.println("任务开始执行");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new TaskWorkException("报错了");
            }
//            return 1;
            throw new TaskWorkException("报错了");
        }
    }

}
