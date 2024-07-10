package org.nexus.base.future;

import java.util.HashMap;

/**
 * @author Xieningjun
 * @date 2024/2/28 1:26
 * @description
 */
// TODO: 2024/2/28 扫包的时候把command加载进来
public class FutureManager extends HashMap<String, Future> {

    public <T> Future<T> create(Class<? extends Future> clazz) {
        Future<T> future = null;
        if (clazz.isAssignableFrom(SyncFuture.class)) {
            future = new SyncFuture<>();
        } else if (clazz.isAssignableFrom(AsyncFuture.class)) {
            future = new AsyncFuture<>();
        }
        // TODO: 2024/3/6
        assert future != null;
        super.put(future.getTrace(), future);
        return future;
    }

    public <T> Future<T> finish(String trace, T resp) {
        Future<T> future = super.get(trace);
        future.finish(resp);
        super.remove(trace);
        return future;
    }

}
