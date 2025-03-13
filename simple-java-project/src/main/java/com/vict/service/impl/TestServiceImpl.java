package com.vict.service.impl;

import com.vict.entity.Testtable;
import com.vict.framework.settimeout.annotation.SetTimeout;
import com.vict.framework.settimeout.dto.SetTimeoutDto;
import com.vict.framework.task.simpletask.SimpleTaskRun;
import com.vict.framework.utils.IdUtils;
import com.vict.framework.utils.UserContextUtil;
import com.vict.mapperservice.TesttableMapperService;
import com.vict.service.TestService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service("testService")
public class TestServiceImpl implements TestService {

    @Autowired
    private TesttableMapperService testtableMapperService;

    @SneakyThrows
    @Override
    public void test() {
        if(System.currentTimeMillis() % 5L == 0){
            int a = 2 / 0;
        }
        Thread.sleep(10000L);
        log.info("测试");
    }

    @Override
    public void insert() {
        Testtable testtable = new Testtable();
        testtable.setId(IdUtils.snowflakeId());
        testtable.setMoney(new BigDecimal("100"));
        testtableMapperService.save(testtable);
    }

    @SimpleTaskRun(failRetryTimeSeconds = {5, 10})
    @Override
    public void testSimpleTask() {
        String requestId = UserContextUtil.getContext().getRequestId();
        System.out.println("requestId:" + requestId);
        log.info("测试simpleTask");
        throw new RuntimeException("测试异常");
    }

    @SetTimeout
    @Override
    public void testSetTimeout(SetTimeoutDto setTimeoutDto) {
        String requestId = UserContextUtil.getContext().getRequestId();
        log.info("requestId:" + requestId);
    }
}
