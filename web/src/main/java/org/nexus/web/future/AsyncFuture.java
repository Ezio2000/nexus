package org.nexus.web.future;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Xieningjun
 * @date 2024/3/6 11:01
 * @description
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
public class AsyncFuture<T> extends Future<T> {

    @Override
    public void await() {
    }

    @Override
    public void finish(T resp) {
    }

}
