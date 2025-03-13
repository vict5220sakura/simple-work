package com.vict.service;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.vict.framework.keyvalue.entity.KeyValue;
import com.vict.framework.FrameworkCommon;
import com.vict.utils.KeyValueUtil;
import com.vict.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class EsService {
    public void indexClear() {
        if(FrameworkCommon.log_es_enable == false){
            return;
        }

        long now = System.currentTimeMillis();

        KeyValue logSaveDays = KeyValueUtil.getValueByKey(KeyValueUtil.key_logSaveDays);
        Integer days = Optional.ofNullable(logSaveDays).map(o -> o.getValue1()).map(o -> o.trim()).map(o -> Integer.valueOf(o)).orElse(3);

        LocalDateTime nowDate = TimeUtil.convert(now);

        LocalDateTime localDateTime = nowDate.minusDays(Math.abs(days));

        for(int i = 0 ; i < 10 ; i++){
            try{
                localDateTime = localDateTime.minusDays(1);
                Long convert = TimeUtil.convert(localDateTime);
                String date = TimeUtil.getTimeStr("yyyy_MM_dd", convert);
                String indexName = "log_" + date;

                HttpRequest httpRequest = HttpUtil.createRequest(Method.DELETE, FrameworkCommon.log_es_url + "/" + indexName);
                httpRequest.basicAuth(FrameworkCommon.log_es_username, FrameworkCommon.log_es_password);
                httpRequest.execute();
            }catch(Exception e){
                log.error("", e);
            }

        }
    }
}
