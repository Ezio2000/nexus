package org.nexus.mysql.interceptor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.nexus.mysql.NexusUser;
import org.springframework.stereotype.Component;

/**
 * @author Xieningjun
 * @date 2024/7/15 19:30
 */
@Component
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class LogInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        NexusUser parameter = (NexusUser) args[1];
        if ("insert".equalsIgnoreCase(mappedStatement.getSqlCommandType().name())) {
            System.out.println(mappedStatement.getBoundSql(parameter).getSql());
        }
        return invocation.proceed();
    }

}
