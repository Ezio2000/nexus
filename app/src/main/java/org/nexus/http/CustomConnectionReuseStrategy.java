package org.nexus.http;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;

/**
 * @author Xieningjun
 * @date 2024/9/11 17:30
 * @description
 */
public class CustomConnectionReuseStrategy implements ConnectionReuseStrategy {

    @Override
    public boolean keepAlive(HttpResponse response, HttpContext context) {
        return false;
    }

}
