package com.vict.framework.fastjsonserializer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.vict.utils.EnumUtils;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnumListDeserializer implements ObjectDeserializer {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        try{
            JSONArray jsonArray = Optional.ofNullable(parser).map(o -> o.parse(fieldName))
                    .map(o -> (JSONArray) o).orElse(new JSONArray());
            // 列表
            List<Object> list = new ArrayList<Object>();
            for(int i = 0; i < jsonArray.size() ; i++){
                Object oItem = null;

                int finalI = i;
                String itemString = Optional.ofNullable(jsonArray).map(o -> o.getString(finalI)).orElse(null);

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
                list.add(oItem);
            }
            return (T)list;
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }
}
