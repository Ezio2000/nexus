package org.nexus.core.life.task;

import lombok.Data;
import org.nexus.core.life.ex.TaskWorkException;
import org.nexus.web.future.Future;
import org.nexus.web.future.SyncFuture;

/**
 * @author Xieningjun
 * @date 2024/8/15 10:06
 * @description
 */
@Data
public abstract class Task {

     protected final Future<?> future;

     public Task(Future<?> future) {
          this.future = future;
     }

     public Task() {
          this.future = new SyncFuture<>();
     }

     public void await() throws InterruptedException {
          if (!future.isWaited()) {
               future.await();
          }
     }

     abstract Object doTask() throws TaskWorkException;

}
