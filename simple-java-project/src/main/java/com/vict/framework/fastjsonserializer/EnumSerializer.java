package com.vict.framework.fastjsonserializer;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Optional;

// 序列化
public class EnumSerializer implements ObjectSerializer {

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        try{
            Method getValue = object.getClass().getMethod("getValue", null);
            String value = Optional.ofNullable(object)
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

            Method getName = object.getClass().getMethod("getName", null);
            String name = Optional.ofNullable(object)
                    .map(o -> {
                        try {
                            return getName.invoke(o, null);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        return null;
                    })
                    .map(o -> (String)o)
                    .orElse(null);

            serializer.write(value);
            serializer.out.write(",\"" + fieldName + "Name" + "\":\"" + name + "\"");
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
