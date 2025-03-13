package com.vict.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @version1.0
 */
public class ObjectUtil {
    public static String toString(Object o){
        try{
            return JSONObject.toJSONString(o);
        }catch(Exception e){}
        try{
            return JSONArray.toJSONString(o);
        }catch(Exception e){}
        return "";
    }
}
