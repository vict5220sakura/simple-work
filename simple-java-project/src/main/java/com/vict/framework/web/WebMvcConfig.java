package com.vict.framework.web;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.vict.framework.FrameworkCommon;
import com.vict.framework.web.filecache.WebFileCache;
import com.vict.framework.web.filecache.entity.FileCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /** 配置fastjson */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 1. 定义一个convert转换消息的对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

        // 2. 添加fastJson的配置信息

        // 3. 处理中文乱码问题
        List<MediaType> supportMediaTypeList = new ArrayList<>();
        supportMediaTypeList.add(MediaType.APPLICATION_JSON_UTF8);
        fastConverter.setSupportedMediaTypes(supportMediaTypeList);

        fastConverter.setDefaultCharset(StandardCharsets.UTF_8);

        // 4. 在converters中添加fastjson的配置信息
        converters.add(0, fastConverter);
    }

    /** 配置全局接口请求前缀 */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix(FrameworkCommon.apiPath,
                c -> c.isAnnotationPresent(ApiPrePath.class)
        );
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

                if(FrameworkCommon.webStaticUseCache == false){
                    // 未启用缓存
                    return true;
                }

                String netFileUri = request.getRequestURI();
                //log.info("请求静态资源, {}", resourceFileUri);
                if((FrameworkCommon.serverContextPath + "web" + "/index.html").equals(netFileUri)){
                    return true;
                }
                if((FrameworkCommon.serverContextPath + "app" + "/index.html").equals(netFileUri)){
                    return true;
                }

                String origin = request.getHeader("origin");
                String referer = request.getHeader("referer");


                // 存在缓存直接返回
                FileCache fileCache = WebFileCache.selectFileCache(netFileUri);
                if(fileCache != null){
                    // 存在缓存
                    //log.info("存在缓存, {}, {}", resourceFileUri, fileCache.getRedirectUrl());
                    if((origin != null && origin.startsWith("https")) || (referer != null && referer.startsWith("https"))){
                        String redirectUrl = fileCache.getRedirectUrl();
                        redirectUrl = redirectUrl.replaceFirst("http", "https");
                        response.setStatus(302);
                        response.addHeader("Location", redirectUrl);
                        return true;
                    }else{
                        response.setStatus(302);
                        response.addHeader("Location", fileCache.getRedirectUrl());
                        return true;
                    }

                }
                return true;
            }
        });
    }
}
