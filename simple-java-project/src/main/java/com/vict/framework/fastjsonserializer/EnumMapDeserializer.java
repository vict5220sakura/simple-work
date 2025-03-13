package com.vict.framework.fastjsonserializer;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.vict.utils.EnumUtils;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EnumMapDeserializer implements ObjectDeserializer {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        try{
            JSONObject jsonObject = Optional.ofNullable(parser).map(o -> o.parse(fieldName))
                    .map(o -> (JSONObject) o).orElse(new JSONObject());
            // 列表
            HashMap<T, T> map = new HashMap<>();

            for(Map.Entry<String, Object> item : jsonObject.entrySet()){
                Object oItem = null;

                String itemString = Optional.ofNullable(item).map(o -> o.getKey()).orElse(null);

                Method getNameMethod = null;
                Method getValueMethod = null;
                String value = Optional.ofNullable(itemString).map(o -> o.toString()).orElse(null);
                if(value == null || value.trim().equals("")){
                    oItem = null;
                }
                Method valuesMethod = null;
                Enum[] enums = null;
                try {

                    getNameMethod = ((Class)((ParameterizedTypeImpl) type).getActualTypeArguments()[0]).getMethod("getName");
                    getValueMethod = ((Class)((ParameterizedTypeImpl) type).getActualTypeArguments()[0]).getMethod("getValue");
                    valuesMethod = ((Class)((ParameterizedTypeImpl) type).getActualTypeArguments()[0]).getMethod("values");
                    enums = (Enum[])valuesMethod.invoke(null, null);
                    Method valueOfMethod = ((Class)((ParameterizedTypeImpl) type).getActualTypeArguments()[0]).getMethod("valueOf", String.class);
                    Object invoke = valueOfMethod.invoke(null, value);

                    oItem = (T)invoke;
                } catch (Exception e) {
                    Enum anEnum = EnumUtils.getEnum(enums, getNameMethod, getValueMethod, value);

                    if(anEnum != null){
                        oItem = (T)anEnum;
                    }
                }
                map.put((T)oItem, (T)oItem);
            }
            return (T)map;
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}
