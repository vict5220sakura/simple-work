package com.vict.framework.utils;

import com.alibaba.fastjson.JSONObject;
import com.vict.bean.applogin.dto.AppToken;
import com.vict.bean.buserlogin.dto.BUserToken;
import com.vict.framework.bean.UserContext;
import com.vict.framework.utils.cache.CacheUtils;
import com.vict.utils.ThreadUtil;

import java.util.Optional;

/**
 * 上下文对象工具
 */
public class UserContextUtil {

    public static void setContext(String token, String aToken, String requestId){

        UserContext userContext = new UserContext();

        if(requestId == null){
            requestId = IdUtils.getSalt(32, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray());
        }
        userContext.setRequestId(requestId);
        userContext.setToken(token);
        userContext.setAToken(aToken);

        // 未登录直接返回
        if((token == null || token.trim().equals("")) && (aToken == null || aToken.trim().equals(""))){
            ThreadUtil.putThreadVariable("userContext", userContext);
            return;
        }

        Long bUserId = findBuserIdByToken(token);
        Long appCustomerId = findAppCustomerIdByAToken(aToken);

        userContext.setToken(token);
        userContext.setToken(aToken);
        userContext.setBuserId(bUserId);
        userContext.setAppCustomerId(appCustomerId);

        ThreadUtil.putThreadVariable("userContext", userContext);
    }

    public static Long findBuserIdByToken(String token){
        // 后台用户token
        String value = CacheUtils.selectCache("token_" + token);
        BUserToken bUserToken = Optional.ofNullable(value).map(o -> o.trim()).filter(o -> !o.equals(""))
                .map(o -> JSONObject.parseObject(o, BUserToken.class)).orElse(null);
        Long bUserId = Optional.ofNullable(bUserToken).map(o -> o.getBUserId()).orElse(null);
        return bUserId;
    }

    public static Long findAppCustomerIdByAToken(String aToken){
        String value = CacheUtils.selectCache("appToken_" + aToken);
        AppToken appToken = Optional.ofNullable(value).map(o -> o.trim()).filter(o -> !o.equals(""))
                .map(o -> JSONObject.parseObject(o, AppToken.class)).orElse(null);
        Long appCustomerId = Optional.ofNullable(appToken).map(o -> o.getCustomer()).map(o -> o.getId()).orElse(null);
        return appCustomerId;
    }

    public static void setContext(UserContext userContext){
        if(userContext == null){
            return;
        }
        ThreadUtil.putThreadVariable("userContext", userContext);
    }

    public static UserContext getContext(){
        Object userContext = ThreadUtil.getThreadVariable("userContext");
        if(userContext == null){
            return null;
        }
        return (UserContext)userContext;
    }

    public static boolean isBUserLogin(){
        UserContext context = getContext();
        if(context == null){
            return false;
        }
        if(context.getBuserId() != null){
            return true;
        }
        return false;
    }

    public static boolean isAppUserLogin(){
        UserContext context = getContext();
        if(context == null){
            return false;
        }
        if(context.getAppCustomerId() != null){
            return true;
        }
        return false;
    }

    public static boolean isLogin(){
        if(isBUserLogin()){
            return true;
        }
        if(isAppUserLogin()){
            return true;
        }
        return false;
    }
}
