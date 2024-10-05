package org.nexus.http;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Xieningjun
 * @date 2024/9/11 16:05
 * @description
 */
public class HttpTest {

    public static void main(String[] args) throws IOException {
        DnsResolver dnsResolver = new CustomDnsResolver();
        SchemePortResolver schemePortResolver = new CustomSchemePortResolver();
        ConnectionReuseStrategy connectionReuseStrategy = new CustomConnectionReuseStrategy();
        HttpClient httpClient = HttpClientBuilder.create()
                .setDnsResolver(dnsResolver)  // 路由
                .setSchemePortResolver(schemePortResolver)  // 选port，原本默认是80和443
                .setConnectionReuseStrategy(connectionReuseStrategy)  // 连接复用
//                .setTargetAuthenticationStrategy()  // 身份认证策略
                .setUserAgent("xieningjun")  // 用户信息，默认是org.apache.http.client
//                .setConnectionBackoffStrategy().setBackoffManager()  // 限流熔断
//                .setRetryHandler()  // 根据异常选择重试策略
//                .setRedirectStrategy()  // 设置重定向策略 1、判断响应是否需要重定向 2、重定向对象
//                .setServiceUnavailableRetryStrategy()  // 处理503服务器未响应的策略
//                .addInterceptorFirst(new HttpRequestInterceptor() {})  // 请求拦截，添加到httpProcessor里
//                .addInterceptorLast(new HttpResponseInterceptor() {})  // 响应拦截，添加到httpProcessor里
//                .setDefaultHeaders()  // 设置默认请求头
                .build();
        /*
        ClientExecChain execChain = createMainExec(
                requestExecCopy,
                connManagerCopy,
                reuseStrategyCopy,
                keepAliveStrategyCopy,
                new ImmutableHttpProcessor(new RequestTargetHost(), new RequestUserAgent(userAgentCopy)),
                targetAuthStrategyCopy,
                proxyAuthStrategyCopy,
                userTokenHandlerCopy);
         这段源码中可以自定义ClientExecChain
         主要区别：
            职责不同：
            ClientExecChain 主要负责 HTTP 请求的执行，包括请求的路由和执行的过程。它是请求执行链的核心组件。
            HttpProcessor 主要负责处理和修改 HTTP 消息的协议级别内容，处理请求和响应的头部和主体。
            使用场景不同：
            ClientExecChain 适用于在请求和响应的执行过程中插入自定义逻辑，例如自定义的认证、日志记录或其他处理。
            HttpProcessor 适用于处理和修改 HTTP 消息的内容，例如添加请求头、解析响应头等。
            工作方式不同：
            ClientExecChain 是在请求执行时调用的，通常用于对请求和响应进行处理和管理。
            HttpProcessor 是在 HTTP 消息的处理过程中调用的，专注于消息内容的修改和处理
         */
        HttpResponse response = httpClient.execute(new HttpPost("http://ruqi-gateway-server/openApi/all"){
            { setConfig(RequestConfig.custom().setSocketTimeout(1000).build()); }
        });
        String result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        System.out.println(result);

//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
//        ResponseEntity<Object> entity = restTemplate.postForEntity("http://ruqi-gateway-server/openApi/all", null, Object.class);
//        System.out.println(entity.getBody());
    }

}
