package org.nexus.core.life.task;

import lombok.Data;
import org.nexus.web.future.Future;

import java.util.Vector;

/**
 * @author Xieningjun
 * @date 2024/8/15 10:06
 * @description
 */
@Data
public abstract class Task {

     protected final Future<?> future;

}
