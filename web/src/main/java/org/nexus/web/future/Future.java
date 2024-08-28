package org.nexus.web.future;

import lombok.Data;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * @author Xieningjun
 * @date 2024/3/6 10:11
 * @description
 */
// todo 如果直接getResp的话，会可能得到e，导致类型转换错误
@Data
public abstract class Future<T> {

    protected String trace = UUID.randomUUID().toString();

    protected T resp;

    protected CountDownLatch latch = new CountDownLatch(1);

    public abstract void await() throws InterruptedException;

    public abstract void finish(T resp);

    public boolean isStarted() {
        return latch.getCount() == 0;
    }

    public boolean isFinished() {
        return resp != null;
    }

    public boolean isThrowable() {
        return resp instanceof Throwable;
    }

}
