package com.vict.framework.mybatishandler;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Slf4j
@MappedJdbcTypes(JdbcType.VARCHAR) // 数据库中该字段存储的类型
@MappedTypes(JSONObject.class) // 需要转换的对象
public class JSONObjectTypeHandler extends BaseTypeHandler<JSONObject> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, JSONObject parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.toJSONString());
    }

    @Override
    public JSONObject getNullableResult(ResultSet rs, String columnName) throws SQLException {
        JSONObject obj = Optional.ofNullable(rs.getString(columnName)).map(o -> o.trim())
                .map(o -> JSONObject.parseObject(o)).orElse(null);
        return obj;
    }

    @Override
    public JSONObject getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        JSONObject obj = Optional.ofNullable(rs.getString(columnIndex)).map(o -> o.trim())
                .map(o -> JSONObject.parseObject(o)).orElse(null);
        return obj;
    }

    @Override
    public JSONObject getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        JSONObject obj = Optional.ofNullable(cs.getString(columnIndex)).map(o -> o.trim())
                .map(o -> JSONObject.parseObject(o)).orElse(null);
        return obj;

    }
}
