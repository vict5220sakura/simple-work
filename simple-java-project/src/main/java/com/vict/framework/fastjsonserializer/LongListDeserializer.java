package com.vict.framework.fastjsonserializer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LongListDeserializer implements ObjectDeserializer {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONArray jsonArray = Optional.ofNullable(parser).map(o -> o.parse(fieldName))
                .map(o -> (JSONArray) o).orElse(new JSONArray());
        // 列表
        List<Long> list = new ArrayList<Long>();
        for(int i = 0; i < jsonArray.size() ; i++){
            int finalI = i;
            Long value = Optional.ofNullable(jsonArray).map(o -> o.getString(finalI))
                    .map(o-> o.trim())
                    .filter(o-> !o.equals(""))
                    .map(o -> new Long(o))
                    .orElse(null);
            list.add(value);
        }
        return (T)list;
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}
