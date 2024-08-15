package org.nexus.core.life.ex;

import org.nexus.base.ex.NexusException;

/**
 * @author Xieningjun
 * @date 2024/8/15 11:12
 * @description
 */
// todo 完善
public class TaskWorkException extends NexusException {

    public TaskWorkException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public TaskWorkException(String msg) {
        super(msg);
    }

}
