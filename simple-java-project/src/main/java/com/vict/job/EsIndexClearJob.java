package com.vict.job;

import com.vict.framework.utils.lock.LockApi;
import com.vict.service.EsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
@EnableScheduling
public class EsIndexClearJob {

    @Autowired
    private EsService esService;

    /**
     * 检查过期es索引并删除
     */
    @Scheduled(cron = "0 0 1,23 * * ?")
    private void start() {
        LockApi esIndexClearJob = LockApi.getLock("EsIndexClearJob");
        try{
            esIndexClearJob.lockBlock();
            esService.indexClear();
        }finally{
            esIndexClearJob.unlockIfSuccess();
        }
    }
}
