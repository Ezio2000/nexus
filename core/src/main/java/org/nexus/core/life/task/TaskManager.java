package org.nexus.core.life.task;

import org.nexus.base.factory.SingletonFactory;
import org.nexus.core.life.ex.TaskWorkException;
import org.nexus.web.future.FutureManager;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.*;

/**
 * @author Xieningjun
 * @date 2024/8/15 10:02
 * @description
 */
public class TaskManager {

    private final List<Task> tasks = new Vector<>();

    private final FutureManager futureManager = SingletonFactory.factory().generate(
            FutureManager.class, FutureManager::new
    );

    /* boss线程 */
    protected ScheduledExecutorService bossExecutor = Executors.newScheduledThreadPool(1);

    /* work线程 */
    protected ExecutorService workerExecutor = Executors.newFixedThreadPool(10);

    public TaskManager() {
        bossExecutor.scheduleAtFixedRate(this::manage, 0, 0, TimeUnit.MILLISECONDS);
    }

    public void accept(Task task) {
        tasks.add(task);
    }

    /**
     * 轮询任务队列是否变化，管理任务队列
     * 1. 已完成的任务删除
     * 2. 未完成的任务执行
     */
    private synchronized void manage() {
        for (Task task : tasks) {
            if (!task.future.isFinished()) {
                workerExecutor.submit(() -> {
                    try {
                        work(task);
                    } catch (TaskWorkException e) {
                        futureManager.finish(task.future.getTrace(), e);
                    }
                });
            }
            if (task.future.isFinished()) {
                tasks.remove(task);
            }
        }
    }

    private void work(Task task) throws TaskWorkException {
        // 业务逻辑
        Object resp = "1";
        // 完成future
        futureManager.finish(task.future.getTrace(), resp);
    }

}
