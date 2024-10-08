package org.nexus.storage;

/**
 * @author Xieningjun
 */
public interface Exchanger<T> {

    T inflow();

    void outflow(T t);

}
