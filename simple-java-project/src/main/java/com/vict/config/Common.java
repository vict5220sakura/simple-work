package com.vict.config;

import com.vict.framework.FrameworkCommon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Slf4j
@Configuration
@Component("common")
@Order(1)
@DependsOn("frameworkCommon")
public class Common {
    public static Environment environment;

    public static Long loginMillSeconds;

    public static Long appLoginMillSeconds;

    @Autowired
    public void setEnvironment(Environment environment){
        Common.environment = environment;
    }

    @PostConstruct
    public void init(){
        loginMillSeconds = Optional.ofNullable(Common.environment.getProperty("loginMillSeconds")).map(o-> Long.valueOf(o))
                .orElse(null);
        appLoginMillSeconds = Optional.ofNullable(Common.environment.getProperty("appLoginMillSeconds")).map(o-> Long.valueOf(o))
                .orElse(null);
    }
}
