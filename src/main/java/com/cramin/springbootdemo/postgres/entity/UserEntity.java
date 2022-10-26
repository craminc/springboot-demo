package com.cramin.springbootdemo.postgres.entity;

import lombok.Builder;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "t_user")
@Data
@Builder
public class UserEntity {

    @Id
    @KeySql(useGeneratedKeys = true)
    @Column(name = "id", insertable = false, updatable = false)
    private Integer id;

    private String name;

    private Short gender;

    private Date birth;
}
