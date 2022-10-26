package com.cramin.springbootdemo.postgres.mapper;

import com.cramin.springbootdemo.postgres.entity.UserEntity;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository("postgresUserMapper")
public interface UserMapper extends Mapper<UserEntity> {
}
