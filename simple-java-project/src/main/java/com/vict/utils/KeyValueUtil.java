package com.vict.utils;

import com.vict.framework.keyvalue.entity.KeyValue;
import com.vict.framework.keyvalue.service.KeyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KeyValueUtil {

    public static final String key_logSaveDays = "logSaveDays";

    private static KeyValueService keyValueService;

    @Autowired
    public void setKeyValueService(KeyValueService keyValueService) {
        KeyValueUtil.keyValueService = keyValueService;
    }

    public static boolean addOrUpdateByKey(KeyValue keyValue) {
        keyValueService.insertOrUpdate(
                keyValue.getKey(),
                keyValue.getValue1(),
                keyValue.getValue2(),
                keyValue.getDesc(),
                keyValue.getHiddenFlag());
        return true;
    }
    public static KeyValue getValueByKey(String key) {
        KeyValue response = keyValueService.getKeyValueByKey(key);
        return response;
    }
    public static List<KeyValue> getValueByKeys(List<String> keys) {
        List<KeyValue> keyValueResponseDtos = keyValueService.getKeyValueByKeys(keys);
        return keyValueResponseDtos;
    }
}
