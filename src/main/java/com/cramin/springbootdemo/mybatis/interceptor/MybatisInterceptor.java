package com.cramin.springbootdemo.mybatis.interceptor;


import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
@Component
public class MybatisInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();

        Field field = ReflectionUtils.findField(args[1].getClass(), "mobile");
        // not exist filed mobile or mobile is null
        if (field == null || field.get(args[1]) == null) return invocation.proceed();

        BoundSql boundSql = mappedStatement.getBoundSql(args[1]);
        String sql = boundSql.getSql();
        if (SqlCommandType.INSERT == sqlCommandType) {

        }
        if (SqlCommandType.UPDATE == sqlCommandType) {

        }
        return invocation.proceed();
    }
}
