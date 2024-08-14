package org.nexus.core.life;

/**
 * @author Xieningjun
 * @date 2024/8/13 14:03
 * @description 生命周期
 */
public interface Lifecycle {

    void bootstrap();

    void connect();

    void active();

    void disconnect();

    void destroy();

}
