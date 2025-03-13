package com.vict.framework.keyvalue.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vict.framework.keyvalue.entity.KeyValue;
import com.vict.framework.keyvalue.bean.ao.KeyValueListAO;
import com.vict.framework.FrameworkCommon;
import com.vict.framework.utils.IdUtils;
import com.vict.framework.keyvalue.mapper.KeyValueMapper;
import com.vict.framework.keyvalue.service.KeyValueService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @Description: 字典操作通用接口
 * @Author: Mr.Shi
 * @version: 1.0
 * @Date: 2024/02/2024/2/28 16:57
 * @copyright: 内蒙古柒个贰航空旅游有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */
@Service
public class KeyValueServiceImpl extends ServiceImpl<KeyValueMapper, KeyValue> implements KeyValueService {

    @Override
    public int insertKeyValue(KeyValue request) {
        KeyValue keyValue = BeanUtil.copyProperties(request, KeyValue.class);
        keyValue.setId(IdUtils.snowflakeId());
        return baseMapper.insert(keyValue);
    }

    @Override
    public void insertOrUpdate(String key, String value1, String value2, String desc, Integer hiddenFlag) {
        KeyValue keyValue = new KeyValue();
        keyValue.setId(IdUtils.snowflakeId());
        keyValue.setKey(key);
        keyValue.setValue1(value1);
        keyValue.setValue2(value2);
        keyValue.setDesc(desc);
        keyValue.setHiddenFlag(hiddenFlag);
        baseMapper.insertOrUpdate(keyValue);
    }

    @Override
    public int deleteKeyValueById(Long id) {
        return baseMapper.deleteById(id);
    }

    @Override
    public int updateKeyValueById(KeyValue request) {
        KeyValue keyValue = BeanUtil.copyProperties(request, KeyValue.class);
        return baseMapper.updateById(keyValue);
    }

    @Override
    public List<KeyValue> getKeyValueList(KeyValueListAO keyValueListAO) {
        // LambdaQueryWrapper<KeyValue> wrapper = new LambdaQueryWrapper<>();
        // wrapper.eq(StringUtils.isNotBlank(request.getKey()),KeyValue::getKey,request.getKey());
        // wrapper.eq(StringUtils.isNotBlank(request.getDesc()),KeyValue::getDesc,request.getDesc());
        // wrapper.eq(StringUtils.isNotBlank(request.getValue1()),KeyValue::getValue1,request.getValue1());
        // wrapper.eq(StringUtils.isNotBlank(request.getValue2()),KeyValue::getValue2,request.getValue2());
        List<KeyValue> keyValueList = baseMapper.selectMyList(keyValueListAO.getKey(), keyValueListAO.getDesc(), 0);
        List<KeyValue> response = null;
        if (CollectionUtil.isNotEmpty(keyValueList)){
            response = BeanUtil.copyToList(keyValueList, KeyValue.class);
        }
        return response;
    }

    @Override
    public KeyValue getKeyValueById(Long id) {
        KeyValue keyValue = baseMapper.selectById(id);
        if (ObjectUtils.isNull(keyValue)){
            return null;
        }
        KeyValue response = BeanUtil.copyProperties(keyValue, KeyValue.class);
        return response;
    }

    @Override
    public KeyValue getKeyValueByKey(String key) {
        KeyValue keyValue = baseMapper.getKeyValueByKey(key);
        KeyValue response = BeanUtil.copyProperties(keyValue, KeyValue.class);
        return response;
    }

    @Override
    public List<KeyValue> getKeyValueByKeys(List<String> keys) {
        List<KeyValue> keyValues = baseMapper.getKeyValueByKeys(keys);
        return keyValues;
    }

    @Override
    public void reloadKeyValueNoAuthKey() {
        KeyValue keyValue_no_auth_key = this.getKeyValueByKey("keyValue_no_auth_key");
        JSONArray keyValue_no_auth_keyArr = Optional.ofNullable(keyValue_no_auth_key).map(o -> o.getValue1())
                .map(o -> o.trim())
                .filter(o -> !o.equals(""))
                .map(o -> JSONArray.parseArray(o))
                .orElse(new JSONArray());
        FrameworkCommon.keyValueNoAuthKeyList = keyValue_no_auth_keyArr.toJavaList(String.class);
    }
}
