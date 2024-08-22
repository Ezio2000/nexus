package org.nexus.core.life.task;

import lombok.Data;
import org.nexus.base.factory.SingletonFactory;
import org.nexus.core.life.ex.TaskWorkException;
import org.nexus.web.future.Future;
import org.nexus.web.future.FutureManager;
import org.nexus.web.future.SyncFuture;

/**
 * @author Xieningjun
 * @date 2024/8/15 10:06
 * @description
 */
@Data
public abstract class Task {

     protected final Future<?> future;

     private final FutureManager futureManager = SingletonFactory.factory().generate(
             FutureManager.class, FutureManager::new
     );

     public Task(Future<?> future) {
          this.future = future;
     }

     public Task() {
          future = futureManager.create(SyncFuture.class);
     }

     public void await() throws InterruptedException {
          if (!future.isWaited()) {
               future.await();
          }
     }

     protected abstract Object doTask() throws TaskWorkException;

}
