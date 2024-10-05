package org.nexus.http;

import org.apache.http.conn.DnsResolver;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Xieningjun
 * @date 2024/9/11 11:32
 * @description
 */
public class CustomDnsResolver implements DnsResolver {

    private final Map<String, DnsAddress[]> dnsCache = new HashMap<>() {
        {
            put("ruqi-gateway-server", new DnsAddress[] {
                    new DnsAddress("172.24.1.33", 34004, 1),
                    new DnsAddress("10.80.33.38", 34004, 1)
            });
        }
    };

    @Override
    public InetAddress[] resolve(String host) throws UnknownHostException {
        return new InetAddress[] {
                InetAddress.getByName(dnsCache.get(host)[0].getHost()),
                InetAddress.getByName(dnsCache.get(host)[1].getHost())
        };
    }

}
