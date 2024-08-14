package org.nexus.core.device;

import org.nexus.web.client.NettyClient;

/**
 * @author Xieningjun
 * @date 2024/7/10 13:46
 */
public class Rail {

    private final String host;

    private final int port;

    private final NettyClient conn;

    public Rail(String host, int port) {
        this.host = host;
        this.port = port;
        this.conn = new NettyClient(host, port);
    }

    public void start() {
        this.conn.start();
    }

}
