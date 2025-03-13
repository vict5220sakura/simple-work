package com.vict.utils;

import cn.hutool.core.util.HexUtil;
import java.security.MessageDigest;

public class SHA1Util {
    public static String wxSha1(String str) {
        try{
            MessageDigest instance = MessageDigest.getInstance("SHA-1");
            instance.update(str.getBytes("UTF-8"));

            byte messageDigest[] = instance.digest();
            String s = HexUtil.encodeHexStr(messageDigest);

            return s;
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
