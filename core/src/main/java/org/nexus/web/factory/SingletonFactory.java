package org.nexus.web.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * @author Xieningjun
 * @date 2024/4/15 10:52
 * @description A factory to build singleton fast.
 */
public class SingletonFactory {

    private static final class InstanceHolder {
        private static final SingletonFactory INSTANCE = new SingletonFactory();
    }

    public static SingletonFactory factory() {
        return InstanceHolder.INSTANCE;
    }

    private final Map<Class<?>, Object> clazzSingletons = new ConcurrentHashMap<>();

    private final Map<String, Object> keySingletons = new ConcurrentHashMap<>();

    public <T> T generate(Class<T> clazz, Supplier<T> constructorFunc) {
        return (T) clazzSingletons.computeIfAbsent(clazz, k -> constructorFunc.get());
    }

    public <T> T generate(String key, Supplier<T> constructorFunc) {
        return (T) keySingletons.computeIfAbsent(key, k -> constructorFunc.get());
    }

    public boolean destroy(Class<?> clazz) {
        return clazzSingletons.remove(clazz) != null;
    }

    public boolean destroy(String key) {
        return keySingletons.remove(key) != null;
    }

    public <T> T search(Class<T> clazz) {
        return (T) clazzSingletons.get(clazz);
    }

    public <T> T search(String key) {
        return (T) keySingletons.get(key);
    }

}
