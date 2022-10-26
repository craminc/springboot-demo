package com.cramin.springbootdemo.database;

import com.cramin.springbootdemo.mysql.entity.UserEntity;
import com.cramin.springbootdemo.mysql.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class DatabaseTests {

    @Autowired
    @Qualifier("mysqlUserMapper")
    private UserMapper userMapper1;

    @Autowired
    @Qualifier("postgresUserMapper")
    private com.cramin.springbootdemo.postgres.mapper.UserMapper userMapper2;

    @Test
    void multiDataSourceTest() {
        List<UserEntity> users1 = userMapper1.selectAll();
        List<com.cramin.springbootdemo.postgres.entity.UserEntity> users2 = userMapper2.selectAll();

        System.out.println(users1);
        System.out.println(users2);
    }
}
