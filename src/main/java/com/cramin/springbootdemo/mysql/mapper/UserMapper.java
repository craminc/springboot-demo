package com.cramin.springbootdemo.mysql.mapper;

import com.cramin.springbootdemo.mysql.entity.UserEntity;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository("mysqlUserMapper")
public interface UserMapper extends Mapper<UserEntity> {
}
