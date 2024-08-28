import org.nexus.base.factory.SingletonFactory;
import org.nexus.web.future.Future;
import org.nexus.web.future.FutureManager;
import org.nexus.web.future.SyncFuture;

/**
 * @author Xieningjun
 * @date 2024/8/28 13:57
 * @description
 */
public class FutureTest {

    public static final FutureManager futureManager = SingletonFactory.factory().generate(
            FutureManager.class, FutureManager::new
    );

    public static void main(String[] args) throws InterruptedException {
        Future future = futureManager.create(SyncFuture.class);
        new Thread(() -> futureManager.finish(future, 0)).start();
        future.await();
        System.out.println(future.isStarted());
    }

}
