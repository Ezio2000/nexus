package org.nexus.mysql.interceptor;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Xieningjun
 * @date 2024/8/2 15:47
 * @description
 */
@Component
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class UpdateInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        String sqlBefore = boundSql.getSql();
        Statement statement = CCJSqlParserUtil.parse(sqlBefore);

        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        if (SqlCommandType.UPDATE != mappedStatement.getSqlCommandType()) {
            return invocation.proceed();
        }

        Update updateStmt = (Update) statement;
        List<Column> columns = updateStmt.getColumns();
        Iterator<Column> iterator = columns.iterator();
        String tableName = updateStmt.getTable().toString();
        List<ParameterMapping> copyParameterMappings = new ArrayList<>(boundSql.getParameterMappings());
        List<Expression> copyExpressions = new ArrayList<>(updateStmt.getExpressions());
        System.out.println(copyExpressions);
        System.out.println(copyExpressions);
        return invocation.proceed();
    }

    public <T> T realTarget(Object target) {
        if (Proxy.isProxyClass(target.getClass())) {
            MetaObject metaObject = SystemMetaObject.forObject(target);
            return realTarget(metaObject.getValue("h.target"));
        }
        return (T) target;
    }

}
