package com.cramin.springbootdemo.postgres.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

//@Configuration
//@MapperScan(basePackages = "com.cramin.springbootdemo.postgres.mapper", sqlSessionFactoryRef = "postgresSqlSessionFactory")
public class PostgresDataSourceConfig {

//    @Bean("postgresDataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.postgres")
    public DataSource dataSource() {
        return DruidDataSourceBuilder.create().build();
    }

//    @Bean("postgresTransactionManager")
    public DataSourceTransactionManager dataSourceTransactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

//    @Bean("postgresSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        return sqlSessionFactoryBean.getObject();
    }
}
