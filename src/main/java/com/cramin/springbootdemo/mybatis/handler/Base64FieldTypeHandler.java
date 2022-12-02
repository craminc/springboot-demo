package com.cramin.springbootdemo.mybatis.handler;

import cn.hutool.core.codec.Base64;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * usage: @ColumnType(typeHandler = Base64FieldTypeHandler.class)
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
public class Base64FieldTypeHandler extends BaseTypeHandler<String> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, Base64.encode(parameter));
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return Base64.decodeStr(rs.getString(columnName));
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return Base64.decodeStr(rs.getString(columnIndex));
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return Base64.decodeStr(cs.getString(columnIndex));
    }
}
