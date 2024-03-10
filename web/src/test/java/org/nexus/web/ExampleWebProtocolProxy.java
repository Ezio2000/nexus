package org.nexus.web;

import lombok.extern.slf4j.Slf4j;
import org.nexus.web.anno.Nexus;
import org.nexus.web.anno.WebProtocol;

import java.util.Arrays;
import java.util.List;

/**
 * @author Xieningjun
 * @date 2024/3/9 15:45
 * @description
 */
@Slf4j
@WebProtocol(nexus = @Nexus(name = "exampleWebProtocolProxy"))
public class ExampleWebProtocolProxy {

    @WebProtocol.Client(uri = "/example")
    public ExampleResp serverReceive(ExampleReq exampleReq) {
        ExampleResp exampleResp = new ExampleResp();
        exampleResp.respCode = exampleReq.reqCode + 1;
        exampleResp.str = exampleReq.str;
        exampleResp.lists = Arrays.asList("谢宁筠", "黄慧芳");
        return exampleResp;
    }

    @WebProtocol.Server(uri = "/example")
    public ExampleResp clientReceive(ExampleResp exampleResp) {
        log.info(
                "收到请求: {}, {}, {}",
                exampleResp.respCode,
                exampleResp.str,
                exampleResp.lists
        );
        return exampleResp;
    }

    static class ExampleReq {
        public int reqCode;
        public String str;
    }

    static class ExampleResp {
        public int respCode;
        public String str;
        List<String> lists;
    }

}
