package org.nexus.common.ex;

/**
 * @author Xieningjun
 * @date 2024/2/27 13:11
 * @description 断言类
 */
public class Assert {

    public static void isTrue(boolean expression) {
        if (!expression) {
            throw new NexusRuntimeException("Expression is false.");
        }
    }

}
