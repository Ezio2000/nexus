package org.nexus.web.pojo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @author Xieningjun
 * @date 2024/3/10 20:28
 * @description
 */
@Data
@Slf4j
public abstract class NexusProxy {

    protected String name;

    protected Class<?> clazz;

    protected Object instance;

    public Object invoke(Method method, Object[] args) throws Throwable {
        log.info("Invoke method {}.", method.getName());
        return method.invoke(this.instance, args);
    }

}
