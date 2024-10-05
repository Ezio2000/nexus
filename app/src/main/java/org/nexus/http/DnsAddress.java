package org.nexus.http;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Xieningjun
 * @date 2024/9/11 15:00
 * @description
 */
@AllArgsConstructor
@Data
public class DnsAddress {

    private String host;

    private int port;

    private int weight;

}
