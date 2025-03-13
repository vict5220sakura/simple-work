package com.vict.framework.fastjsonserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.vict.utils.EnumUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Optional;

public class EnumDeserializer implements ObjectDeserializer {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        Method getNameMethod = null;
        Method getValueMethod = null;
        String value = Optional.ofNullable(parser.parse(fieldName)).map(o -> o.toString()).orElse(null);
        if(value == null || value.trim().equals("")){
            return null;
        }
        Method valuesMethod = null;
        Enum[] enums = null;
        try {
            getNameMethod = ((Class)type).getMethod("getName");
            getValueMethod = ((Class)type).getMethod("getValue");
            valuesMethod = ((Class)type).getMethod("values");
            enums = (Enum[])valuesMethod.invoke(null, null);

            Method valueOfMethod = ((Class)type).getMethod("valueOf", String.class);
            Object invoke = valueOfMethod.invoke(null, value);

            return (T)invoke;
        } catch (Exception e) {

            Enum anEnum = EnumUtils.getEnum(enums, getNameMethod, getValueMethod,value);

            if(anEnum != null){
                return (T)anEnum;
            }

            throw new RuntimeException(e);
        }
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}
