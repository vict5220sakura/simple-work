package com.vict.framework.mybatishandler;

import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@MappedJdbcTypes(JdbcType.VARCHAR) // 数据库中该字段存储的类型
@MappedTypes(List.class) // 需要转换的对象
public class ListLongTypeHandler extends BaseTypeHandler<List> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List list, JdbcType jdbcType) throws SQLException {
        ps.setString(i, JSONArray.toJSONString(list));
    }

    @Override
    public List<Long> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        List<Long> longs = Optional.ofNullable(rs.getString(columnName)).map(o -> o.trim())
                .map(o -> JSONArray.parseArray(o)).map(o -> o.toJavaList(Long.class)).orElse(null);
        return longs;
    }

    @Override
    public List<Long> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        List<Long> longs = Optional.ofNullable(rs.getString(columnIndex)).map(o -> o.trim())
                .map(o -> JSONArray.parseArray(o)).map(o -> o.toJavaList(Long.class)).orElse(null);
        return longs;
    }

    @Override
    public List<Long> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        List<Long> longs = Optional.ofNullable(cs.getString(columnIndex)).map(o -> o.trim())
                .map(o -> JSONArray.parseArray(o)).map(o -> o.toJavaList(Long.class)).orElse(null);
        return longs;

    }
}
