package com.vict.framework.filter;

import com.alibaba.fastjson.JSONObject;
import com.vict.framework.Constants;
import com.vict.framework.FrameworkCommon;
import com.vict.framework.bean.R;
import com.vict.framework.utils.UserContextUtil;
import com.vict.utils.PatternUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Slf4j
@Component
@Order(3)
public class FrameworkFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = ((HttpServletRequest)servletRequest);
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(req);
        HttpServletResponse res = ((HttpServletResponse)servletResponse);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(res);

        // 取token数据初始化上下文
        String token = req.getHeader("token");
        String aToken = req.getHeader("aToken");
        String requestId = req.getHeader("requestId");

        UserContextUtil.setContext(token, aToken, requestId);

        // 定义表示变量 并验证用户请求URI 是否包含不过滤路径
        boolean flag = false;

        boolean logFlag = true;

        for(String notAuthUrl : FrameworkCommon.notAuthUrls){
            if(PatternUtil.check(notAuthUrl, req.getRequestURI())){
                flag = true;
                break;
            }
        }

        for(String notLogUrl : FrameworkCommon.notLogUrls){
            if(PatternUtil.check(notLogUrl, req.getRequestURI())){
                logFlag = false;
                break;
            }
        }

        // web不过滤
        if(PatternUtil.check(
                "^" + FrameworkCommon.serverContextPath + "web.*" + "$",
                req.getRequestURI())){
            flag = true;
            logFlag = false;
        }

        // app不过滤
        if(PatternUtil.check(
                "^" + FrameworkCommon.serverContextPath + "app.*" + "$",
                req.getRequestURI())){
            flag = true;
            logFlag = false;
        }

        // swagger不过滤
        if(PatternUtil.check("^.*swagger.*$", req.getRequestURI())){
            flag = true;
            logFlag = false;
        }
        if(PatternUtil.check("^.*webjars.*$", req.getRequestURI())){
            flag = true;
            logFlag = false;
        }
        if(PatternUtil.check("^.*api-docs.*$", req.getRequestURI())){
            flag = true;
            logFlag = false;
        }
        if(PatternUtil.check("^.*/axure.*$", req.getRequestURI())){
            flag = true;
            logFlag = false;
        }

        // inner不过滤
        if(PatternUtil.check("^.*/inner-service/inner$", req.getRequestURI())){
            flag = true;
        }

        // job不过滤
        if(PatternUtil.check("^.*/job-service/control$", req.getRequestURI())){
            flag = true;
        }
        if(PatternUtil.check("^.*/job-service/handActionJob$", req.getRequestURI())){
            flag = true;
        }

        // runLog不过滤
        if(PatternUtil.check("^.*/runLog.*$", req.getRequestURI())){
            flag = true;
        }

        // 健康检查不过滤
        if(PatternUtil.check("^.*/health/heartbeat$", req.getRequestURI())){
            flag = true;
        }


        if(!flag){ // 需要过滤
            boolean tokenFlag = UserContextUtil.isLogin(); // 鉴权

            if(tokenFlag == false){ // 鉴权不通过
                res.addHeader("Access-Control-Allow-Credentials", "true");
                res.addHeader("Access-Control-Allow-Origin", "*");
                res.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
                res.addHeader("Access-Control-Allow-Headers", "Content-Type,X-CAF-Authorization-Token,sessionToken,X-TOKEN,token,reqId,*");

                servletResponse.setCharacterEncoding("UTF-8");
                servletResponse.setContentType("application/json");
                PrintWriter writer = null;
                try{
                    writer = servletResponse.getWriter();
                    R<Object> r = R.failed(Constants.authFail, Constants.authFailMsg);
                    writer.print(JSONObject.toJSONString(r));

                    if(logFlag){
                        logRequest(requestWrapper);
                        logResponseAuth(JSONObject.toJSONString(r));
                    }

                    return;
                }catch(IOException e){

                }finally{
                    if(writer != null){
                        writer.close();
                    }
                }
            }else{ // 鉴权通过
                filterChain.doFilter(requestWrapper, responseWrapper);
                if(logFlag){
                    // 记录入参
                    logRequest(requestWrapper);
                    logResponse(responseWrapper);
                }
                responseWrapper.copyBodyToResponse();
                return;
            }
        }else{ // 不需要过滤
            filterChain.doFilter(requestWrapper, responseWrapper);
            if(logFlag){
                // 记录入参
                logRequest(requestWrapper);
                logResponse(responseWrapper);
            }
            responseWrapper.copyBodyToResponse();
            return;
        }


    }

    /** 记录日志 */
    @SneakyThrows
    private void logRequest(ContentCachingRequestWrapper requestWrapper) {
        // 记录请求入参
        String requestURI = requestWrapper.getRequestURI();
        String token = requestWrapper.getHeader("token");
        String aToken = requestWrapper.getHeader("aToken");

        String requestParamStr = Optional.ofNullable(requestWrapper).map(o -> o.getParameterMap()).map(o -> JSONObject.toJSONString(o)).orElse(null);
        // 从请求中获取body内容
        String characterEncoding = Optional.ofNullable(requestWrapper).map(o -> o.getCharacterEncoding()).orElse("UTF-8");
        String requestBody = Optional.ofNullable(requestWrapper).map(o -> o.getContentAsByteArray()).map(o -> {
            try {
                return new String(o, characterEncoding);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        }).orElse(null);
        if(requestBody != null && requestBody.length() > 50000){
            requestBody = requestBody.substring(0, 50000);
        }
        JSONObject requestJson = null;
        try{
            requestJson = JSONObject.parseObject(requestBody);
        }catch(Exception e){}


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("requestURI", requestURI);
        jsonObject.put("requestParam", requestParamStr);
        jsonObject.put("token", token);
        jsonObject.put("aToken", aToken);
        if(requestJson != null){
            jsonObject.put("requestBody", requestJson);
        }else{
            jsonObject.put("requestBody", requestBody);
        }

        log.info("请求入参参数:{}", jsonObject.toJSONString());
    }

    /** 记录日志 */
    private void logResponse(ContentCachingResponseWrapper responseWrapper) {

        // 从响应中获取body内容
        // String responseCharacterEncoding = Optional.ofNullable(responseWrapper).map(o -> o.getCharacterEncoding()).orElse("UTF-8");
        String responseCharacterEncoding = "UTF-8";
        String responseBody = Optional.ofNullable(responseWrapper).map(o -> o.getContentAsByteArray())
                .map(o -> {
                    try {
                        return new String(o, responseCharacterEncoding);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).orElse(null);
        if(responseBody != null && responseBody.length() > 50000){
            responseBody = responseBody.substring(0, 50000);
        }
        JSONObject responseJson = null;
        try{
            responseJson = JSONObject.parseObject(responseBody);
        }catch(Exception e){}
        JSONObject jsonObject = new JSONObject();

        if(responseJson != null){
            jsonObject.put("responseBody", responseJson);
        }else{
            jsonObject.put("responseBody", responseBody);
        }

        log.info("请求响应参数:{}", jsonObject.toJSONString());
    }

    /** 记录日志 */
    private void logResponseAuth(String responseStr) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("responseBody", responseStr);

        log.info("请求响应参数:{}", jsonObject.toJSONString());
    }
}
