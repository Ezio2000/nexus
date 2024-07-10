package org.nexus.base.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author Xieningjun
 * @date 2024/3/2 11:09
 * @description
 */
public class Transformer {

    public static <T> T json2Object(String json, Class<T> clazz) {
        return new Gson().fromJson(json, clazz);
    }

    public static <T> T json2Object(FullHttpRequest req, Class<T> clazz) {
        return new Gson().fromJson(
                req.content().toString(StandardCharsets.UTF_8),
                clazz
        );
    }

    public static <T> T json2Object(FullHttpResponse resp, Class<T> clazz) {
        return new Gson().fromJson(
                resp.content().toString(StandardCharsets.UTF_8),
                clazz
        );
    }

    public static <T> List<T> list2Object(FullHttpResponse response, Class<T> clazz) {
        // 从 FullHttpResponse 中获取 content
        ByteBuf content = response.content();
        // 将 ByteBuf 转换为字符串
        String contentString = content.toString(StandardCharsets.UTF_8);
        // 使用 Gson 将 JSON 字符串解析为 List<T>
        return new Gson().fromJson(contentString, TypeToken.getParameterized(List.class, clazz).getType());
    }

    public static String object2Json(Object obj) {
        return new Gson().toJson(obj);
    }

}
