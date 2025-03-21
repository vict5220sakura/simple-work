package com.vict.framework.fastjsonserializer;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Optional;

// 序列化
public class StringSerializer implements ObjectSerializer {

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        String s = Optional.ofNullable(object).map(o -> (String) o).map(o -> o.trim()).orElse(null);
        serializer.write(s);
    }
}
