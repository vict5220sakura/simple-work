package com.vict.framework.fastjsonserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.vict.utils.TimeUtil;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.Optional;

public class TimestampDeserializer implements ObjectDeserializer {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        Timestamp timestamp = Optional.ofNullable(parser.parse(fieldName))
                .map(o -> o.toString())
                .map(o-> TimeUtil.getTime(o))
                .map(o -> new Timestamp(o)).orElse(null);
        return (T)timestamp;
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}
