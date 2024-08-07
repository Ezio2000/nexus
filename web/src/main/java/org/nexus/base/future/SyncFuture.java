package org.nexus.base.future;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

/**
 * @author Xieningjun
 * @date 2024/3/6 10:28
 * @description
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
public class SyncFuture<T> extends Future<T> {

    private CountDownLatch latch = new CountDownLatch(1);

    @Override
    public void await() throws InterruptedException {
        this.latch.await();
    }

    @Override
    public void finish(T resp) {
        super.setResp(resp);
        this.latch.countDown();
    }

}
