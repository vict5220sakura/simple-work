package com.vict.framework.keyvalue.service;

import com.vict.framework.keyvalue.entity.KeyValue;
import com.vict.framework.keyvalue.bean.ao.KeyValueListAO;

import java.util.List;

/**
 * @Description: ***
 * @Author: Mr.Shi
 * @version: 1.0
 * @Date: 2024/02/2024/2/28 16:57
 * @copyright: 内蒙古柒个贰航空旅游有限公司 All rights reserved. Notice
 * 仅限于授权后使用，禁止非授权传阅以及私自用于商业目的。
 */

public interface KeyValueService {

    /**
     * 新增
     * @param keyValue
     * @return
     */
    int insertKeyValue(KeyValue keyValue);

    void insertOrUpdate(String key, String value1, String value2, String desc, Integer hiddenFlag);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int deleteKeyValueById(Long id);

    /**
     * 根据id修改
     * @return
     */
    int updateKeyValueById(KeyValue keyValue);

    /**
     * 获取列表
     * @return
     */
    List<KeyValue> getKeyValueList(KeyValueListAO keyValueListAO);

    /**
     * 根据id获取
     * @param id
     * @return
     */
    KeyValue getKeyValueById(Long id);

    KeyValue getKeyValueByKey(String key);

    List<KeyValue> getKeyValueByKeys(List<String> keys);

    void reloadKeyValueNoAuthKey();
}
