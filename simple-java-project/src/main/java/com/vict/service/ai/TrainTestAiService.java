package com.vict.service.ai;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.vict.framework.myannotation.MyDescription;
import com.vict.utils.EnumUtils;
import com.vict.utils.PatternUtil;
import com.vict.utils.TimeUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@Service
public class TrainTestAiService {

    @MyDescription("订单(票)类型枚举")
    @Getter
    @AllArgsConstructor
    public enum UserIntentEnum implements IEnum<String> {
        userIntentEnum_train("userIntentEnum_train", "订火车票"),
        userIntentEnum_hotel("userIntentEnum_hotel", "订酒店"),
        userIntentEnum_other("userIntentEnum_other", "其他分类"),
        ;
        private String value;
        private String name;

        @Override
        public String toString() {
            return "UserIntentEnum{" +
                    "value='" + value + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    /** 用户意图分析 */
    @SneakyThrows
    public UserIntentEnum userIntentAnalysis(String message) {
        HttpRequest request = HttpUtil.createRequest(Method.POST, "https://dashscope.aliyuncs.com/api/v1/apps/628e21b8a07947c5ba9f09165db5b63c/completion");
        request.header("Authorization", "Bearer XXX");
        request.header("Content-Type", "application/json");

        request.body("{\n" +
                "    \"input\": {\n" +
                "        \"prompt\": \""+message+"\",\n" +
                "        \"biz_params\": {\n" +
                "            \"message\": \""+message+"\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"parameters\": {},\n" +
                "    \"debug\": {}\n" +
                "}");
        HttpResponse res = request.execute();

        String body = res.body();
        log.info("body: {}" + body);
        try{
            JSONObject jsonObject = JSONObject.parseObject(body);
            String text = jsonObject.getJSONObject("output").getString("text");
            JSONObject jsonObject1 = JSONObject.parseObject(text);
            String type = jsonObject1.getString("type");
            UserIntentEnum enumByKey = EnumUtils.getEnumByKey(UserIntentEnum.class, type);
            return enumByKey;
        }catch(Exception e){
            return null;
        }

    }

    public Long analysisStartTimestamp(String message){
        HttpRequest request = HttpUtil.createRequest(Method.POST, "https://dashscope.aliyuncs.com/api/v1/apps/5d3622bb702f4496a3a9034d405fab60/completion");
        request.header("Authorization", "Bearer sk-c73c931fccbc47f1b22912e8c0bccef0");
        request.header("Content-Type", "application/json");

        request.body("{\n" +
                "    \"input\": {\n" +
                "        \"prompt\": \""+message+"\"\n" +
                "    },\n" +
                "    \"parameters\": {},\n" +
                "    \"debug\": {}\n" +
                "}");
        HttpResponse res = request.execute();

        String body = res.body();
        log.info("body: {}" + body);
        try{
            JSONObject jsonObject = JSONObject.parseObject(body);
            String text = jsonObject.getJSONObject("output").getString("text");
            if(text == null || text.equals("无")){
                return null;
            }else{
                return TimeUtil.getTime(text);
            }
        }catch(Exception e){
            return null;
        }
    }

    @Data
    public class FromTo{
        private String from;
        private String to;
    }

    public FromTo analysisFromTo(String message){
        HttpRequest request = HttpUtil.createRequest(Method.POST, "https://dashscope.aliyuncs.com/api/v1/apps/82d71edfb1bc4a869d86b6dd0a410a2a/completion");
        request.header("Authorization", "Bearer sk-c73c931fccbc47f1b22912e8c0bccef0");
        request.header("Content-Type", "application/json");

        request.body("{\n" +
                "    \"input\": {\n" +
                "        \"prompt\": \""+message+"\"\n" +
                "    },\n" +
                "    \"parameters\": {},\n" +
                "    \"debug\": {}\n" +
                "}");
        HttpResponse res = request.execute();

        String body = res.body();
        log.info("body: {}" + body);
        try{
            JSONObject jsonObject = JSONObject.parseObject(body);
            String text = jsonObject.getJSONObject("output").getString("text");
            if(text == null || text.equals("无")){
                return null;
            }else{
                // 出发地: 包头, 到达地: 北京
                String from = PatternUtil.matcherOne("(?<=出发地:).*(?=,)", text);
                String to = PatternUtil.matcherOne("(?<=到达地:).*$", text);

                from = Optional.ofNullable(from).map(o-> o.trim()).orElse(null);
                to = Optional.ofNullable(to).map(o-> o.trim()).orElse(null);

                if(from.equals("无")){
                    from = null;
                }
                if(to.equals("无")){
                    to = null;
                }

                if(from == null || to == null){
                    return null;
                }


                FromTo fromTo = new FromTo();

                fromTo.setFrom(from);
                fromTo.setTo(to);

                return fromTo;
            }
        }catch(Exception e){
            return null;
        }
    }

    public String analysisAddress(String message){
        HttpRequest request = HttpUtil.createRequest(Method.POST, "https://dashscope.aliyuncs.com/api/v1/apps/e981799081f343d3a15158347caacd2d/completion");
        request.header("Authorization", "Bearer sk-c73c931fccbc47f1b22912e8c0bccef0");
        request.header("Content-Type", "application/json");

        request.body("{\n" +
                "    \"input\": {\n" +
                "        \"prompt\": \""+message+"\"\n" +
                "    },\n" +
                "    \"parameters\": {},\n" +
                "    \"debug\": {}\n" +
                "}");
        HttpResponse res = request.execute();

        String body = res.body();
        log.info("body: {}" + body);
        try{
            JSONObject jsonObject = JSONObject.parseObject(body);
            String text = jsonObject.getJSONObject("output").getString("text");
            return text;
        }catch(Exception e){
            return null;
        }
    }
}
