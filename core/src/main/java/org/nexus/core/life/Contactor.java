package org.nexus.core.life;

/**
 * @author Xieningjun
 * @date 2024/8/13 14:16
 * @description
 */
public interface Contactor {

    void bootstrap(String host, int port);

    void start();

    void shutdown();

    boolean probe();

}
