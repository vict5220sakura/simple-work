package com.vict.utils;

import java.lang.reflect.Method;

public class EnumUtils {
    public static <T extends Enum<T>> T getEnum(T[] enmus, Method getNameMethod, Method getValueMethod, String name) {
        for (T tItem : enmus) {
            try {
                String nameItem = (String)getNameMethod.invoke(tItem);
                String valueItem = (String)getValueMethod.invoke(tItem);
                if(nameItem.equals(name) || valueItem.equals(name)){
                    return tItem;
                };
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static <T extends Enum<T>> T getEnumByKey(Class<T> t, String key){
        if(key == null){
            return null;
        }
        Method getNameMethod = null;
        Method getValueMethod = null;
        Method valuesMethod = null;
        Method valueOfMethod = null;
        Enum[] enums = null;
        try {
            getNameMethod = t.getMethod("getName");
            getValueMethod = t.getMethod("getValue");
            valuesMethod = t.getMethod("values");
            enums = (Enum[])valuesMethod.invoke(null, null);

            valueOfMethod = t.getMethod("valueOf", String.class);
            Object invoke = valueOfMethod.invoke(null, key);

            return (T)invoke;
        } catch (Exception e) {

            Enum anEnum = EnumUtils.getEnum(enums, getNameMethod, getValueMethod, key);

            if(anEnum != null){
                return (T)anEnum;
            }

            return null;
        }
    }
}
