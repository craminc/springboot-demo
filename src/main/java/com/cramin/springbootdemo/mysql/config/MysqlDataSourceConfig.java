package com.cramin.springbootdemo.mysql.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.cramin.springbootdemo.mybatis.interceptor.MybatisInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.cramin.springbootdemo.mysql.mapper", sqlSessionFactoryRef = "mysqlSqlSessionFactory")
public class MysqlDataSourceConfig {

    @Autowired
    private MybatisInterceptor interceptor;

    @Bean("mysqlDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.mysql")
    public DataSource dataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean("mysqlTransactionManager")
    public DataSourceTransactionManager dataSourceTransactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean("mysqlSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        sqlSessionFactoryBean.setPlugins(interceptor);
        return sqlSessionFactoryBean.getObject();
    }
}
