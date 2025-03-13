package com.vict.framework.fastjsonserializer;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.vict.utils.TimeUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.Optional;

// 序列化
public class TimestampSerializer implements ObjectSerializer {

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        String s = Optional.ofNullable(object).map(o -> (Timestamp) o).map(o -> o.getTime()).map(o -> TimeUtil.getTimeStr(o)).orElse(null);
        serializer.write(s);
    }
}
