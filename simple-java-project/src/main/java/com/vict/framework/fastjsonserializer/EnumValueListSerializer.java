package com.vict.framework.fastjsonserializer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// 序列化
public class EnumValueListSerializer implements ObjectSerializer {

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        try{

            Method getValue = ((Class)((ParameterizedTypeImpl) fieldType).getActualTypeArguments()[0]).getMethod("getValue");

            JSONArray arr = new JSONArray();
            List list = Optional.ofNullable(object).map(o -> (List) o).orElse(new ArrayList());

            for(int i = 0 ; i < list.size() ; i++){
                int finalI = i;

                String item = Optional.ofNullable(list)
                        .map(o-> o.get(finalI))
                        .map(o -> {
                            try {
                                return getValue.invoke(o, null);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                            return null;
                        })
                        .map(o -> (String)o)
                        .orElse(null);

                arr.add(item);
            }
            serializer.write(arr);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
