package org.nexus.web.anno;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Xieningjun
 * @date 2024/3/10 20:19
 * @description
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Nexus {

    String name() default "";

}
