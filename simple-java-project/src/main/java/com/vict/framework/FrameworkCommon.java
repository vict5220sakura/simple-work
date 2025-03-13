package com.vict.framework;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.vict.framework.myannotation.MyDescription;
import com.vict.framework.utils.IdUtils;
import com.vict.framework.keyvalue.service.KeyValueService;
import com.vict.utils.PatternUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component("frameworkCommon")
@Order(1)
public class FrameworkCommon {
    public static Environment environment;

    public static String env; // 环境
    public static boolean publicServerFlag = true; // 服务器标识

    public static List<String> notAuthUrls; // 不过滤url
    public static List<String> notLogUrls; // 不过滤url

    public static Long taskScanTime = 30L * 1000L; // 任务执行器扫描频率
    public static Long taskScanTime_extend = ((Double)(taskScanTime.doubleValue() * 2.5)).longValue(); // 任务执行器扫描时间向后偏移量  0-------------<=now + taskScanTime_extend------------oo

    // lock
    public static String lockImpl = "jdk";
    public static final String lockImpl_redis = "redis";
    public static final String lockImpl_jdk = "jdk";
    public static final String lockImpl_mysql = "mysql";
    public static String lockPrx; // 锁前缀

    // cache
    public static String cacheImpl = "jdk";
    public static final String cacheImpl_redis = "redis";
    public static final String cacheImpl_jdk = "jdk";

    // cache
    public static String countImpl = "jdk";
    public static final String countImpl_redis = "redis";
    public static final String countImpl_jdk = "jdk";

    // 参数
    public static String springApplicationName; // 服务名称 serverName

    @MyDescription("/simple-server/")
    public static String serverContextPath; // 服务路径
    public static String serverPort; // 服务端口
    public static String serverId; // 服务id 每个服务一个id
    public static String hostName;

    public static boolean taskSelfFlag; // task只执行自己标记
    public static boolean paySelfFlag; // pay只执行自己标记

    // redis
    public static boolean redis_enable = false; // redis是否启用
    public static String redis_host;
    public static Integer redis_port;
    public static String redis_password;
    public static Integer redis_database;
    public static Integer redis_timeout;
    public static Integer redis_pool_max_active;
    public static Integer redis_pool_max_wait;
    public static Integer redis_pool_max_idle;
    public static Integer redis_pool_minIidle;

    public static boolean log_es_enable = false; // loges是否启用
    public static String log_es_url;
    public static String log_es_username;
    public static String log_es_password;



    private static String taskMysqlUrl;
    private static String taskMysqlUsername;
    private static String taskMysqlPassword;

    public static DataSource taskMysql = null;

    public static String payServerUrl;

    public static int virtualPay; // 虚拟支付开关 0-关 1-成功 2-失败

    public static int jobNum = 10;

    public static String apiPath; // api统一前缀

    public static String qiNiuAk;
    public static String qiNiuSk;
    public static String qiNiuUploadHost;
    public static String qiNiuBucket;
    public static String qiNiuFileUrl;

    public static boolean webStaticUseCache;

    @Autowired
    public void setEnvironment(Environment environment){
        FrameworkCommon.environment = environment;
    }

    private static Map framworkConfigMap;
    private static Map framworkEnvConfigMap;

    public static List<String> keyValueNoAuthKeyList; // keyValue不过滤key列表

    @Autowired
    private KeyValueService keyValueService;

    public static String getFramworkProperty(Map tempMap, String key){

        String[] keyArr = key.split("\\.");

        // Map tempMap = framworkEnvConfigMap;

        for(int i = 0 ; i < keyArr.length ; i++){
            if(i == keyArr.length - 1){
                // 最后一个
                if(tempMap == null){
                    return null;
                }
                Object o = tempMap.get(keyArr[i]);

                if(o instanceof String){
                    return (String)o;
                }else if(o instanceof Integer){
                    return ((Integer)o).toString();
                }else if(o instanceof Double){
                    return ((Double)o).toString();
                }else if(o instanceof Float){
                    return ((Float)o).toString();
                }else if(o instanceof Long){
                    return ((Long)o).toString();
                }else if(o instanceof Boolean){
                    return ((Boolean)o).toString();
                }
            }else{
                if(tempMap == null){
                    return null;
                }
                tempMap = (Map)tempMap.get(keyArr[i]);
            }
        }

        return null;
    }

    public static <T> T  getFramworkProperty(Map tempMap, String key, Class<T> clazz){

        String[] keyArr = key.split("\\.");

        // Map tempMap = framworkEnvConfigMap;

        for(int i = 0 ; i < keyArr.length ; i++){
            if(i == keyArr.length - 1){
                // 最后一个
                if(tempMap == null){
                    return null;
                }
                T o = (T)tempMap.get(keyArr[i]);
                return o;
            }else{
                if(tempMap == null){
                    return null;
                }
                tempMap = (Map)tempMap.get(keyArr[i]);
            }
        }

        return null;
    }

    static{
        try{
            // // 程序初始化
            // 资源读取
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("application.yml");
            Yaml yaml = new Yaml();
            FrameworkCommon.framworkConfigMap = (Map)yaml.load(inputStream);

            ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
            // 全局循环引用消除
            JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask();

            // 程序环境初始化
            String profilesActive = getFramworkProperty(FrameworkCommon.framworkConfigMap, "spring.profiles.active");
            if(profilesActive != null && !profilesActive.trim().equals("")){
                FrameworkCommon.env = profilesActive;
            }
            String systemEnv = System.getenv("SPRING_PROFILES_ACTIVE");
            if(systemEnv != null && !systemEnv.trim().equals("")){
                FrameworkCommon.env = systemEnv;
            }
            String springbootEnv = System.getenv("spring.profiles.active");
            if(springbootEnv != null && !systemEnv.trim().equals("")){
                FrameworkCommon.env = springbootEnv;
            }
            String systemCmd = System.getProperty("sun.java.command");
            try{
                String systemCmdEnv = PatternUtil.matcherOne("(?<=--spring.profiles.active=)[a-zA-Z]+(?=\\s|$)", systemCmd);
                if(systemCmdEnv != null){
                    // String systemCmdEnv = systemCmd2.substring(systemCmd2.length() - 3, systemCmd2.length());
                    if(systemCmdEnv != null && !systemCmdEnv.trim().equals("")){
                        FrameworkCommon.env = systemCmdEnv;
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                String systemCmd3 = PatternUtil.matcherOne("--serverFlag=[a-zA-Z]{10}", systemCmd);
                if(systemCmd3 != null && systemCmd3.length() >= 10){
                    String serverFlagStr = systemCmd3.substring(systemCmd3.length() - 10, systemCmd3.length());
                    if(serverFlagStr != null && serverFlagStr.equals("serverFlag")){
                        FrameworkCommon.publicServerFlag = true;
                    }else{
                        FrameworkCommon.publicServerFlag = false;
                    }
                }else{
                    FrameworkCommon.publicServerFlag = false;
                }
            }catch(Exception e){
                System.out.println();
            }

            // // 框架初始化
            // 读取框架环境yaml
            InputStream envInputStream = classLoader.getResourceAsStream("application-" + FrameworkCommon.env + ".yml");
            Yaml envYaml = new Yaml();
            FrameworkCommon.framworkEnvConfigMap = (Map)envYaml.load(envInputStream);


            // 配置前缀
            log.info("启动环境 {}", FrameworkCommon.env);

            // es配置
            FrameworkCommon.log_es_url = getFramworkProperty(framworkEnvConfigMap, "log.es_url");
            FrameworkCommon.log_es_username = getFramworkProperty(framworkEnvConfigMap, "log.es_username");
            FrameworkCommon.log_es_password = getFramworkProperty(framworkEnvConfigMap, "log.es_password");
            if(FrameworkCommon.log_es_url != null && !FrameworkCommon.log_es_url.trim().equals("")){
                FrameworkCommon.log_es_enable = true;
            }

            // redis参数初始化
            FrameworkCommon.redis_host = getFramworkProperty(framworkEnvConfigMap, "spring.redis.host");
            FrameworkCommon.redis_port = getFramworkProperty(framworkEnvConfigMap, "spring.redis.port", Integer.TYPE);
            FrameworkCommon.redis_password = getFramworkProperty(framworkEnvConfigMap, "spring.redis.password");
            FrameworkCommon.redis_database = getFramworkProperty(framworkEnvConfigMap, "spring.redis.database", Integer.TYPE);
            FrameworkCommon.redis_timeout = getFramworkProperty(framworkEnvConfigMap, "spring.redis.timeout", Integer.TYPE);
            FrameworkCommon.redis_pool_max_active = getFramworkProperty(framworkEnvConfigMap, "spring.redis.lettuce.pool.max-active", Integer.TYPE);
            FrameworkCommon.redis_pool_max_wait = getFramworkProperty(framworkEnvConfigMap, "spring.redis.lettuce.pool.max-wait", Integer.TYPE);
            FrameworkCommon.redis_pool_max_idle = getFramworkProperty( framworkEnvConfigMap, "spring.redis.lettuce.pool.max-idle", Integer.TYPE);
            FrameworkCommon.redis_pool_minIidle =  getFramworkProperty( framworkEnvConfigMap, "spring.redis.lettuce.pool.min-idle", Integer.TYPE);
            if(FrameworkCommon.redis_host != null && !FrameworkCommon.redis_host.trim().equals("")){
                FrameworkCommon.redis_enable = true;
            }

            // hostName初始化
            InetAddress localhost = null;
            try {
                localhost = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            FrameworkCommon.hostName = localhost.getHostName();

            // 服务id 初始化
            FrameworkCommon.serverId = IdUtils.getSalt(64, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray());

            FrameworkCommon.apiPath = getFramworkProperty(framworkConfigMap, "apiPath");

            FrameworkCommon.qiNiuAk = getFramworkProperty(framworkEnvConfigMap, "qiNiuAk");
            FrameworkCommon.qiNiuSk = getFramworkProperty(framworkEnvConfigMap, "qiNiuSk");
            FrameworkCommon.qiNiuUploadHost = getFramworkProperty(framworkEnvConfigMap, "qiNiuUploadHost");
            FrameworkCommon.qiNiuBucket = getFramworkProperty(framworkEnvConfigMap, "qiNiuBucket");
            FrameworkCommon.qiNiuFileUrl = getFramworkProperty(framworkEnvConfigMap, "qiNiuFileUrl");
            FrameworkCommon.webStaticUseCache = getFramworkProperty(framworkEnvConfigMap, "webStaticUseCache", Boolean.class);
        }catch(Exception eall){
            eall.printStackTrace();
        }

    }

    @SneakyThrows
    @PostConstruct
    public void init(){
        // // 程序初始化

        // 是否自身task
        if(FrameworkCommon.publicServerFlag == false) { // 不是服务器
            String taskSelfFlag = Optional.ofNullable(FrameworkCommon.environment.getProperty("taskSelfFlag")).map(o -> o.trim()).filter(o -> !o.equals("")).orElse(null);
            if(taskSelfFlag != null){
                FrameworkCommon.taskSelfFlag = true;
            }else{
                FrameworkCommon.taskSelfFlag = false;
            }
        }else{
            // 是服务器
            FrameworkCommon.taskSelfFlag = false;
        }

        // 是否自身 pay
        if(FrameworkCommon.publicServerFlag == false) { // 不是服务器
            String paySelfFlag = Optional.ofNullable(FrameworkCommon.environment.getProperty("paySelfFlag")).map(o -> o.trim()).filter(o -> !o.equals("")).orElse(null);
            if(paySelfFlag != null){
                FrameworkCommon.paySelfFlag = true;
            }else{
                FrameworkCommon.paySelfFlag = false;
            }
        }else{
            // 是服务器
            FrameworkCommon.paySelfFlag = false;
        }

        // 不需要过滤url正则表达式
        String notAuthUrls = FrameworkCommon.environment.getProperty("notAuthUrls");
        List<String> notAuthUrlList = Optional.ofNullable(notAuthUrls).map(o -> o.split(","))
                .filter(o -> o.length > 0)
                .map(o -> Arrays.asList(o)).orElse(new ArrayList<>());
        FrameworkCommon.notAuthUrls = notAuthUrlList.stream().map(o-> o.trim()).collect(Collectors.toList());

        // 不需要记录日志url正则表达式
        String notLogUrls = FrameworkCommon.environment.getProperty("notLogUrls");
        List<String> notLogUrlList = Optional.ofNullable(notLogUrls).map(o -> o.split(","))
                .filter(o -> o.length > 0)
                .map(o -> Arrays.asList(o)).orElse(new ArrayList<>());
        FrameworkCommon.notLogUrls = notLogUrlList.stream().map(o-> o.trim()).collect(Collectors.toList());

        // 虚拟支付
        FrameworkCommon.virtualPay = Optional.ofNullable(FrameworkCommon.environment.getProperty("virtualPay")).map(o -> o.trim()).filter(o -> !o.equals(""))
                .map(o -> Integer.valueOf(o)).orElse(0);

        // lock实现方式
        if(redis_enable){
            FrameworkCommon.lockImpl = FrameworkCommon.environment.getProperty("lockImpl"); // 必须配置redis
            if(FrameworkCommon.lockImpl == null){
                FrameworkCommon.lockImpl = FrameworkCommon.lockImpl_jdk;
            }
        }else{
            FrameworkCommon.lockImpl = FrameworkCommon.lockImpl_jdk;
        }

        // cache实现方式
        if(redis_enable){
            FrameworkCommon.cacheImpl = FrameworkCommon.environment.getProperty("cacheImpl"); // 必须配置redis
            if(FrameworkCommon.cacheImpl == null){
                FrameworkCommon.cacheImpl = FrameworkCommon.cacheImpl_jdk;
            }
        }else{
            FrameworkCommon.cacheImpl = FrameworkCommon.cacheImpl_jdk;
        }

        // count实现方式
        if(redis_enable){
            FrameworkCommon.countImpl = FrameworkCommon.environment.getProperty("countImpl"); // 必须配置redis
            if(FrameworkCommon.countImpl == null){
                FrameworkCommon.countImpl = FrameworkCommon.countImpl_jdk;
            }
        }else{
            FrameworkCommon.countImpl = FrameworkCommon.countImpl_jdk;
        }

        // 服务名称
        FrameworkCommon.springApplicationName = FrameworkCommon.environment.getProperty("spring.application.name");
        if(FrameworkCommon.springApplicationName == null){
            throw new RuntimeException("没有配置spring.application.name");
        }

        FrameworkCommon.serverContextPath = FrameworkCommon.environment.getProperty("server.servlet.context-path");
        FrameworkCommon.serverPort = FrameworkCommon.environment.getProperty("server.port");

        // lock前缀 初始化
        FrameworkCommon.lockPrx = FrameworkCommon.environment.getProperty("spring.application.name");

        log.info("framworkCommon初始化完成");

        log.info("serverId:{}", FrameworkCommon.serverId);

        String swaggerUrl = "http://127.0.0.1:" + serverPort;
        swaggerUrl += serverContextPath + "swagger-ui.html#/";
        log.info("swagger-url:{}", swaggerUrl);


        String jobNum = FrameworkCommon.environment.getProperty("jobNum");
        if(jobNum != null && !jobNum.trim().equals("")){
            FrameworkCommon.jobNum = Integer.valueOf(jobNum);
        }


        keyValueService.reloadKeyValueNoAuthKey();

        String indexUrl = "http://127.0.0.1:" + serverPort;
        indexUrl += serverContextPath + "web";
        log.info("index:{}", indexUrl);
    }
}
