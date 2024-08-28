package org.nexus.core.life.task;

import org.nexus.base.factory.SingletonFactory;
import org.nexus.core.life.ex.TaskFinishException;
import org.nexus.core.life.ex.TaskWaitException;
import org.nexus.core.life.ex.TaskWorkException;
import org.nexus.core.life.resour.Releaser;
import org.nexus.web.future.FutureManager;

import java.util.concurrent.*;

/**
 * @author Xieningjun
 * @date 2024/8/15 10:02
 * @description
 */
public class TaskManager implements Releaser {

    private final CopyOnWriteArrayList<Task> tasks = new CopyOnWriteArrayList<>();

    private final FutureManager futureManager = SingletonFactory.factory().generate(
            FutureManager.class, FutureManager::new
    );

    /* boss线程 */
    protected ScheduledExecutorService bossExecutor = Executors.newScheduledThreadPool(2);

    /* work线程 */
    protected ExecutorService workerExecutor = Executors.newFixedThreadPool(10);

    public TaskManager() {
        bossExecutor.scheduleAtFixedRate(this::work, 0, 1, TimeUnit.MILLISECONDS);
        bossExecutor.scheduleAtFixedRate(this::unwork, 0, 1, TimeUnit.MILLISECONDS);
    }

    public void accept(Task task) {
        tasks.add(task);
    }

    /**
     * 轮询任务队列是否变化，管理任务队列
     * 1. 已完成的任务删除
     * 2. 未完成的任务执行
     */
    private synchronized void work() {
        // todo 每个task要有自己的锁
        for (Task task : tasks) {
            workerExecutor.submit(() -> {
                try {
                    // 业务逻辑
                    // 外部可以task.await上锁
                    Object resp = task.doTask();
                    // 完成future
                    futureManager.finish(task.future, resp);
                } catch (TaskWorkException e) {
                    futureManager.finish(task.future.getTrace(), e);
                } catch (TaskWaitException | TaskFinishException ignored) {}
            });
        }
    }

    /**
     * 删除结束的任务
     */
    private synchronized void unwork() {
        tasks.removeIf(task -> task.future.isFinished());
    }

    @Override
    public void release() {
        bossExecutor.shutdown();
        workerExecutor.shutdown();
        tasks.clear();
    }

}
