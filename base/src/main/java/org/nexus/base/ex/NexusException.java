package org.nexus.base.ex;

import lombok.extern.slf4j.Slf4j;
import org.nexus.base.anno.Disposal;

/**
 * @author Xieningjun
 * @date 2024/2/27 13:09
 * @description Nexus运行时异常
 */
@Slf4j
public class NexusException extends Exception implements Disposal {

    public NexusException(String msg) {
        super(msg);
    }

    public NexusException(String msg, Throwable cause) {
        super(msg, cause);
    }

    @Override
    public void dispose() {
        Throwable t = super.getCause() == null ? this : super.getCause();
        log.error("Dispose nexus exception.", t);
    }

}
