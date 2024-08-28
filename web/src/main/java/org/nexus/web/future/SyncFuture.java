package org.nexus.web.future;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Xieningjun
 * @date 2024/3/6 10:28
 * @description
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
public class SyncFuture<T> extends Future<T> {

    SyncFuture() {}

    @Override
    public void await() throws InterruptedException {
        latch.await();
    }

    @Override
    public void finish(T resp) {
        super.setResp(resp);
        latch.countDown();
    }

}
