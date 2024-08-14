package org.nexus.core.device;

/**
 * @author Xieningjun
 * @date 2024/7/10 13:37
 * @description
 */
public abstract class Device {

    protected String host;

    protected String port;

    public Device(String host, String port) {
        this.host = host;
        this.port = port;
    }

}
