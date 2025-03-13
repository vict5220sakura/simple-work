package com.vict.framework.fastjsonserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;
import java.util.Optional;

public class IntegerDeserializer implements ObjectDeserializer {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        Integer l = Optional.ofNullable(parser.parse(fieldName)).map(o -> o.toString()).filter(o-> !o.equals("")).map(o -> Integer.parseInt(o)).orElse(null);
        return (T)l;
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}
