import io.netty.handler.codec.http.HttpHeaders;
import org.nexus.core.device.Device;
import org.nexus.core.life.task.ContactTask;
import org.nexus.web.client.Contactor;
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
            @Override
            public Object command(String path, HttpHeaders header, Object reqBody) {
                return reqBody.toString();
            }
        };
        Device device = new Device("org.nexus.com", 44060, contactor);
        device.bootstrap();
        device.connect();
        // todo trace相同导致取消时重复了
        Task task1 = new TestTask();
        Task task2 = new TestTask();
        Task task3 = new TestTask();
        Task task4 = new ContactTask(contactor, "1", null, "宁军");
        device.accept(task1);
        device.accept(task2);
        task1.await();
        task2.await();
        System.out.println(task1.getFuture().getResp());
        System.out.println(task2.getFuture().getResp());
        // todo 现在是可以提交两次的，要限制吗
        if (device.accept(task3)) {
            task3.await();
            System.out.println(task3.getFuture().getResp());
        }
        if (device.accept(task4)) {
            task4.await();
            System.out.println(task4.getFuture().getResp());
        }
        device.disconnect();
        device.destroy();
    }

    public static class TestTask extends Task {
        @Override
        protected Object doTask0() throws Throwable {
            try {
                System.out.println("任务开始执行");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new Exception("报错了");
            }
//            return 1;
            throw new Exception("报错了");
        }
    }

}
