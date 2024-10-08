import org.nexus.executor.VirtualSubjectExecutor;
import org.nexus.subject.Subject;

import java.util.concurrent.CountDownLatch;

/**
 * @author Xieningjun
 */
public class AbsSubjectTest {

    public static void main(String[] args) throws InterruptedException {
        System.out.println(Thread.currentThread());
        var executor = new VirtualSubjectExecutor();
        var latch = new CountDownLatch(3);
        var subject = new Subject() {
            @Override
            public String key() {
                return "http.request";
            }
            @Override
            public Object run0() throws Throwable {
                Thread.sleep(1000);
                System.out.println(Thread.currentThread() + ": run0");
                return "request";
            }
            @Override
            public void afterResult(Object result) {
                System.out.println(Thread.currentThread() + ": " +  result);
                System.out.println(executor.scrape());
                latch.countDown();
            }
            @Override
            public void afterError(Throwable t) {
                System.out.println(Thread.currentThread());
                t.printStackTrace();
                System.out.println(executor.scrape());
                latch.countDown();
            }
        };
        executor.sync(subject);
        executor.async(subject);
        executor.schedule(subject, 100, 1);
        latch.await();
    }

}
