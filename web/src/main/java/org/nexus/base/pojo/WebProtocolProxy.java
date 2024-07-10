package org.nexus.base.pojo;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.nexus.base.util.Transformer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Xieningjun
 * @date 2024/3/10 20:47
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class WebProtocolProxy extends NexusProxy {

    protected String uri;

    protected Method reqHandler;

    protected Method respHandler;

    public Object handleReq(FullHttpRequest req) throws Throwable {
        List<Object> args = new ArrayList<>();
        for (Class<?> clazz : this.reqHandler.getParameterTypes()) {
            args.add(Transformer.json2Object(req, clazz));
        }
        return super.invoke(this.reqHandler, args.toArray());
    }

    public Object handleResp(FullHttpResponse resp) throws Throwable {
        List<Object> args = new ArrayList<>();
        for (Class<?> clazz : this.respHandler.getParameterTypes()) {
            args.add(Transformer.json2Object(resp, clazz));
        }
        return super.invoke(this.respHandler, args.toArray());
    }

}
