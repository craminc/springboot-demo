package com.cramin.springbootdemo.mysql.entity;

import lombok.Builder;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "user")
@Data
@Builder
public class UserEntity {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    private String name;

    private Integer age;

    private Date createTime;

    private Date updateTime;
}
