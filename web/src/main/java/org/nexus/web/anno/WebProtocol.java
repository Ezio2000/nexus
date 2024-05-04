package org.nexus.web.anno;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Xieningjun
 * @date 2024/3/10 20:19
 * @description
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface WebProtocol {

    String name();

    String uri();

    enum Role {
        SERVER, CLIENT
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface Client {
        Role role() default Role.CLIENT;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface Server {
        Role role() default Role.SERVER;
    }

}
