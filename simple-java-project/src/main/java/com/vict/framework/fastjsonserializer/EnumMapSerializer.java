package com.vict.framework.fastjsonserializer;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

// 序列化
public class EnumMapSerializer implements ObjectSerializer {

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        try{

            Method getName = ((Class)((ParameterizedTypeImpl) fieldType).getActualTypeArguments()[0]).getMethod("getName");

            JSONObject jsonMap = new JSONObject();
            Map map = Optional.ofNullable(object).map(o -> (Map) o).orElse(new HashMap<>());
            Set keySet = map.keySet();

            for(Object key : keySet){
                String item = Optional.ofNullable(map)
                        .map(o-> o.get(key))
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

                jsonMap.put(item, item);
            }
            serializer.write(jsonMap);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
