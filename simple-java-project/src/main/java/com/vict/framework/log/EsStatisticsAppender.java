package com.vict.framework.log;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSONObject;
import com.vict.framework.FrameworkCommon;
import com.vict.framework.bean.UserContext;
import com.vict.framework.utils.IdUtils;
import com.vict.framework.utils.UserContextUtil;
import com.vict.utils.TimeUtil;
import lombok.Data;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * elk配置 log4j配置
 */
@Plugin(name = "EsStatistics", category = "Core", elementType = "appender", printObject = true)
public class EsStatisticsAppender extends AbstractAppender {

    /** 日志级别大于等于此级别及以上会进行判断错误。默认：ERROR */
    private String failedOnLogLevel;
    /** 指定时间内，出现多少次该日志级别，会被认为是错误。默认：10 */
    private Integer failedOnLogLevelCount;
    /** 该日志级别以上持续出现多长时间，会被认为是错误。默认:30000 */
    private Integer failedOnLogLevelInMisSecond;
    /** 当连续小于该日志级别多长时间后，恢复为正常状态。默认：120000 */
    private Integer recoveryOnLessLogLevelInMisSecond;


    protected EsStatisticsAppender(String name, Filter filter, Layout<? extends Serializable> layout,
                                   String failedOnLogLevel,
                                   Integer failedOnLogLevelCount,
                                   Integer failedOnLogLevelInMisSecond,
                                   Integer recoveryOnLessLogLevelInMisSecond) {
        super(name, filter, layout);
        this.failedOnLogLevel = failedOnLogLevel;
        this.failedOnLogLevelCount = failedOnLogLevelCount;
        this.failedOnLogLevelInMisSecond = failedOnLogLevelInMisSecond;
        this.recoveryOnLessLogLevelInMisSecond = recoveryOnLessLogLevelInMisSecond;
    }

    public static ConcurrentLinkedQueue<LogDto> logList = new ConcurrentLinkedQueue<LogDto>();
    public static ExecutorService logThreadPool = new ThreadPoolExecutor(1, 1, 99999, TimeUnit.DAYS, new ArrayBlockingQueue<>(20));

    private static Map<String, Boolean> esIndexExistMap = new ConcurrentHashMap<String, Boolean>();

    /** 判断es索引是否存在 */
    public static boolean checkEsIndexExist(String indexName){
        Boolean cache = esIndexExistMap.get(indexName);
        if(cache != null){
            return cache;
        }
        HttpRequest httpRequest = HttpUtil.createGet(FrameworkCommon.log_es_url + "/" + indexName + "/_search?size=0");
        httpRequest.basicAuth(FrameworkCommon.log_es_username, FrameworkCommon.log_es_password);
        HttpResponse execute = httpRequest.execute();
        String body = execute.body();

        JSONObject jsonObject = JSONObject.parseObject(body);
        JSONObject error = jsonObject.getJSONObject("error");
        if(error == null){
            // 存在
            esIndexExistMap.put(indexName, true);
            return true;
        }else{
            // 不存在
            return false;
        }
    }

    public static void createEsINdex(String indexName){
        HttpRequest httpRequest = HttpUtil.createRequest(Method.PUT, FrameworkCommon.log_es_url + "/" + indexName);
        httpRequest.basicAuth(FrameworkCommon.log_es_username, FrameworkCommon.log_es_password);
        httpRequest.header("Content-Type", "application/json");

        httpRequest.body(
                "{\n" +
                "    \"settings\": {\n" +
                "        \"number_of_shards\": 5,\n" +
                "        \"number_of_replicas\": 1\n" +
                "    },\n" +
                "    \"mappings\": {\n" +
                "        \"properties\": {\n" +
                "            \"timestamp\": {\n" +
                "                \"type\": \"date\"\n" +
                "            },\n" +
                "            \"serverName\": {\n" +
                "                \"type\": \"keyword\"\n" +
                "            },\n" +
                "            \"token\": {\n" +
                "                \"type\": \"keyword\"\n" +
                "            },\n" +
                "            \"bUserId\": {\n" +
                "                \"type\": \"keyword\"\n" +
                "            },\n" +
                "            \"appUserId\": {\n" +
                "                \"type\": \"keyword\"\n" +
                "            },\n" +
                "            \"level\": {\n" +
                "                \"type\": \"keyword\"\n" +
                "            },\n" +
                "            \"threadId\": {\n" +
                "                \"type\": \"keyword\"\n" +
                "            },\n" +
                "            \"threadName\": {\n" +
                "                \"type\": \"keyword\"\n" +
                "            },\n" +
                "            \"errInfo\": {\n" +
                "                \"type\": \"text\"\n" +
                "            },\n" +
                "            \"text\": {\n" +
                "                \"type\": \"text\"\n" +
                "            },\n" +
                "            \"requestId\": {\n" +
                "                \"type\": \"keyword\"\n" +
                "            },\n" +
                "            \"hostName\": {\n" +
                "                \"type\": \"keyword\"\n" +
                "            },\n" +
                "            \"serverId\": {\n" +
                "                \"type\": \"keyword\"\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}");

        HttpResponse execute = httpRequest.execute();
        execute.body();
    }

    static{
        logThreadPool.submit(()->{
            while(true){
                try {
                    LogDto peek = EsStatisticsAppender.logList.peek();

                    if(peek == null){
                        Thread.sleep(2000L);
                        continue;
                    }

                    StringBuilder sb = new StringBuilder();
                    for(int i = 0 ; i < 100; i++){
                        LogDto logDto = EsStatisticsAppender.logList.poll();
                        if(logDto == null){
                            break;
                        }
                        UserContext context = logDto.getContext();
                        Long time = logDto.getTime();

                        JSONObject jsonObject = new JSONObject();

                        String springApplicationName = FrameworkCommon.springApplicationName;
                        String serverId = FrameworkCommon.serverId;
                        String bUserId = Optional.ofNullable(context).map(o -> o.getBuserId()).map(o-> o.toString()).orElse(null);
                        String appUserId = Optional.ofNullable(context).map(o -> o.getAppCustomerId()).map(o-> o.toString()).orElse(null);
                        String token = Optional.ofNullable(context).map(o -> o.getToken()).orElse(null);
                        String requestId = Optional.ofNullable(context).map(o -> o.getRequestId()).orElse(null);

                        String text = logDto.getText();
                        String level = logDto.getLevel();
                        String threadId = logDto.getThreadId();
                        String threadName = logDto.getThreadName();
                        String errInfo = logDto.getErrInfo();

                        if(text != null && text.length() > 50000){
                            text = text.substring(0, 50000);
                        }

                        jsonObject.put("timestamp", TimeUtil.getTimeStr("yyyy-MM-dd'T'HH:mm:ss.SSS+08:00", time));
                        jsonObject.put("serverName", springApplicationName);
                        jsonObject.put("bUserId", bUserId);
                        jsonObject.put("appUserId", appUserId);
                        jsonObject.put("token", token);
                        jsonObject.put("level", level);
                        jsonObject.put("threadId", threadId);
                        jsonObject.put("threadName", threadName);
                        jsonObject.put("errInfo", errInfo);
                        jsonObject.put("text", text);
                        jsonObject.put("requestId", requestId);
                        jsonObject.put("hostName", FrameworkCommon.hostName);
                        jsonObject.put("serverId", serverId);

                        sb.append("{\"create\":{\"_id\": \""+ IdUtils.getSalt(32, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray())+"\"}}");
                        sb.append("\r\n");
                        sb.append(jsonObject.toJSONString());
                        sb.append("\r\n");
                    }
                    // logDtos.clear();
                    String date = TimeUtil.getTimeStr("yyyy_MM_dd", System.currentTimeMillis());

                    String indexName = "log_" + date;

                    if(!checkEsIndexExist(indexName)){
                        createEsINdex(indexName);
                    }

                    HttpRequest httpRequest = HttpUtil.createPost(FrameworkCommon.log_es_url + "/" + indexName + "/_doc/_bulk");
                    httpRequest.basicAuth(FrameworkCommon.log_es_username, FrameworkCommon.log_es_password);
                    httpRequest.body(sb.toString());
                    httpRequest.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Data
    public static class LogDto{
        private UserContext context;
        private Long time;
        private String level;
        private String threadId;
        private String threadName;
        private String errInfo;
        private String text;
    }

    @Override
    public void append(LogEvent logEvent) {
        String level = Optional.ofNullable(logEvent).map(o-> o.getLevel()).map(o-> o.name()).orElse(null); // 日志级别
        String threadId = Optional.ofNullable(logEvent).map(o-> o.getThreadId()).map(o-> o.toString()).orElse(null); // 线程id
        String threadName = Optional.ofNullable(logEvent).map(o-> o.getThreadName()).orElse(null); // 线程名
        String errInfo = Optional.ofNullable(logEvent).map(o-> o.getThrown()).map(o-> getExceptionAsString(o)).orElse(null);
        String text = Optional.ofNullable(logEvent).map(o-> o.getMessage()).map(o-> o.getFormattedMessage()).orElse(null); // 日志文本信息

        LogDto logDto = new LogDto();
        logDto.setContext(UserContextUtil.getContext());
        logDto.setTime(System.currentTimeMillis());
        logDto.setLevel(level);
        logDto.setThreadId(threadId);
        logDto.setThreadName(threadName);
        logDto.setErrInfo(errInfo);
        logDto.setText(text);
        if(FrameworkCommon.log_es_enable){
            logList.add(logDto);
        }
    }

    private static String getExceptionAsString(Throwable throwable) {
        // 使用 StringWriter 将异常信息转换为字符串
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return stringWriter.toString();
    }

    @PluginFactory
    public static EsStatisticsAppender createAppender(@PluginAttribute("name") String name,
                                                      @PluginElement("Filter") final Filter filter,
                                                      @PluginElement("Layout") Layout<? extends Serializable> layout,
                                                      @PluginAttribute("failedOnLogLevel") String failedOnLogLevel,
                                                      @PluginAttribute("failedOnLogLevelCount") Integer failedOnLogLevelCount,
                                                      @PluginAttribute("failedOnLogLevelInMisSecond") Integer failedOnLogLevelInMisSecond,
                                                      @PluginAttribute("recoveryOnLessLogLevelInMisSecond") Integer recoveryOnLessLogLevelInMisSecond) {
        if (name == null) {
            LOGGER.error("No name provided for MyCustomAppenderImpl");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        if (failedOnLogLevel == null) {
            failedOnLogLevel = "ERROR";
        }
        if (failedOnLogLevelCount == null) {
            failedOnLogLevelCount = 10;
        }
        if (failedOnLogLevelInMisSecond == null) {
            failedOnLogLevelInMisSecond = 30000;
        }
        if (recoveryOnLessLogLevelInMisSecond == null) {
            recoveryOnLessLogLevelInMisSecond = 120000;
        }
        return new EsStatisticsAppender(name, filter, layout, failedOnLogLevel, failedOnLogLevelCount, failedOnLogLevelInMisSecond, recoveryOnLessLogLevelInMisSecond);
    }
}
