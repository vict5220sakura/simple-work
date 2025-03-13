package com.vict.framework.keyvalue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vict.framework.keyvalue.entity.KeyValue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface KeyValueMapper extends BaseMapper<KeyValue> {

    int insertOrUpdate(KeyValue keyValue);

    KeyValue getKeyValueByKey(String key);

    List<KeyValue> getKeyValueByKeys(List<String> keys);

    List<KeyValue> selectMyList(@Param("key")String key, @Param("desc")String desc, @Param("hiddenFlag")Integer hiddenFlag);
}
