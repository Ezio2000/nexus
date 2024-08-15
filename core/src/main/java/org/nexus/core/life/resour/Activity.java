package org.nexus.core.life.resour;

import org.nexus.core.life.task.Task;

/**
 * @author Xieningjun
 * @date 2024/8/14 17:41
 * @description
 */
public interface Activity {

    void accept(Task task);

}
