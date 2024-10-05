package org.nexus.http;

import org.apache.http.HttpHost;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.UnsupportedSchemeException;
import org.apache.http.impl.conn.DefaultSchemePortResolver;
import org.apache.http.util.Args;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Xieningjun
 * @date 2024/9/11 16:06
 * @description
 */
public class CustomSchemePortResolver implements SchemePortResolver {

    private final SchemePortResolver delegate = DefaultSchemePortResolver.INSTANCE;

    private final Map<String, DnsAddress[]> dnsCache = new HashMap<>() {
        {
            put("ruqi-gateway-server", new DnsAddress[] {
                    new DnsAddress("172.24.1.33", 34004, 100),
                    new DnsAddress("10.80.33.38", 34004, 100)
            });
        }
    };

    @Override
    public int resolve(HttpHost host) throws UnsupportedSchemeException {
        Args.notNull(host, "HTTP host");
        try {
            return dnsCache.get(host.getHostName())[0].getPort();
        } catch (Exception e) {
            return delegate.resolve(host);
        }
    }

}
