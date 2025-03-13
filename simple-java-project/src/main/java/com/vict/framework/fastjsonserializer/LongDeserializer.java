package com.vict.framework.fastjsonserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;
import java.util.Optional;

public class LongDeserializer implements ObjectDeserializer {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        Long l = Optional.ofNullable(parser.parse(fieldName)).map(o -> o.toString())
                .map(o-> o.trim()).filter(o-> !o.equals("")).map(o -> Long.parseLong(o)).orElse(null);
        return (T)l;
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}
