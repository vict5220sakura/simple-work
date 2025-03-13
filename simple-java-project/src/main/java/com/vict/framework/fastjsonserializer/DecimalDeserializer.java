package com.vict.framework.fastjsonserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Optional;

public class DecimalDeserializer implements ObjectDeserializer {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        BigDecimal l = Optional.ofNullable(parser.parse(fieldName)).map(o -> o.toString()).map(o -> new BigDecimal(o)).orElse(null);
        return (T)l;
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}
