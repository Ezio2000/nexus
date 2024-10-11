package org.nexus.storage;

/**
 * @author Xieningjun
 */
public interface Exchanger<T> {

    T inflow() throws Throwable;

    void outflow(T t) throws Throwable;

}
