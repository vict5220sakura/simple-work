package com.vict.framework.fastjsonserializer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.vict.utils.TimeUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// 序列化
public class TimestampListSerializer implements ObjectSerializer {

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        JSONArray arr = new JSONArray();
        List list = Optional.ofNullable(object).map(o -> (List) o).orElse(new ArrayList());

        for(int i = 0 ; i < list.size() ; i++){
            int finalI = i;
            String item = Optional.ofNullable(list).map(o-> o.get(finalI)).map(o -> (Timestamp) o).map(o -> o.getTime()).map(o -> TimeUtil.getTimeStr(o)).orElse("");
            arr.add(item);
        }
        serializer.write(arr);
    }
}
