package org.nexus.core.life.task;

import lombok.Data;
import org.nexus.base.factory.SingletonFactory;
import org.nexus.core.life.ex.TaskFinishException;
import org.nexus.core.life.ex.TaskWaitException;
import org.nexus.core.life.ex.TaskWorkException;
import org.nexus.web.future.Future;
import org.nexus.web.future.FutureManager;
import org.nexus.web.future.SyncFuture;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Xieningjun
 * @date 2024/8/15 10:06
 * @description
 */
@Data
public abstract class Task {

     protected final Future<?> future;

     protected static Lock lock = new ReentrantLock();

     protected final FutureManager futureManager = SingletonFactory.factory().generate(
             FutureManager.class, FutureManager::new
     );

     public Task(Future<?> future) {
          this.future = future;
     }

     public Task() {
          future = futureManager.create(SyncFuture.class);
     }

     public void await() throws InterruptedException {
          if (!future.isStarted()) {
               future.await();
          }
     }

     public Object doTask() throws TaskWaitException, TaskFinishException, TaskWorkException {
          if (lock.tryLock()) {
               lock.lock();
          } else {
               throw new TaskWaitException();
          }
          // 如果已执行完或者已开始，则跳过
          if (future.isFinished()) {
               throw new TaskFinishException();
          }
          if (future.isStarted()) {
               throw new TaskWaitException();
          }
          try {
               return doTask0();
          } catch (Throwable t) {
               throw new TaskWorkException(t);
          } finally {
               lock.unlock();
          }
     }

     protected Object doTask0() throws Throwable {
          throw new TaskWorkException(new NullPointerException("Task is undefined."));
     }

}
