package org.nexus.core.life.resour;

import org.nexus.core.life.task.Task;
import org.nexus.core.life.task.TaskManager;

/**
 * @author Xieningjun
 * @date 2024/8/14 17:41
 * @description
 */
public interface Activity {

    TaskManager listen();

    void accept(Task task);

}
