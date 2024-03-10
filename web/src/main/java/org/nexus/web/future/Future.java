package org.nexus.web.future;

import lombok.Data;

/**
 * @author Xieningjun
 * @date 2024/3/6 10:11
 * @description
 */
@Data
public abstract class Future<T> {

    private String trace = String.valueOf(System.currentTimeMillis());

    private T resp;

    public abstract void await() throws InterruptedException;

    public abstract void finish(T resp);

    public boolean finished() {
        return resp != null;
    }

}
