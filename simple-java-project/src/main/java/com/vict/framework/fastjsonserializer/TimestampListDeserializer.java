package com.vict.framework.fastjsonserializer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.vict.utils.TimeUtil;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TimestampListDeserializer implements ObjectDeserializer {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONArray jsonArray = Optional.ofNullable(parser).map(o -> o.parse(fieldName))
                .map(o -> (JSONArray) o).orElse(new JSONArray());
        // 列表
        List<Timestamp> list = new ArrayList<>();
        for(int i = 0; i < jsonArray.size() ; i++){
            int finalI = i;
            Timestamp timestamp = Optional.ofNullable(jsonArray).map(o -> o.getString(finalI))
                    .map(o -> TimeUtil.getTime(o))
                    .map(o -> new Timestamp(o))
                    .orElse(null);
            list.add(timestamp);
        }
        return (T)list;
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}
