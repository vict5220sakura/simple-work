package com.vict.controller.test;


import com.vict.bean.buser.vo.BuserVO;
import com.vict.entity.Buser;
import com.vict.framework.keyvalue.entity.KeyValue;
import com.vict.framework.bean.R;
import com.vict.framework.settimeout.dto.SetTimeoutDto;
import com.vict.framework.task.util.TaskExecuteUtil;
import com.vict.framework.utils.UserContextUtil;
import com.vict.framework.utils.lock.LockMysql;
import com.vict.framework.utils.lock.dao.MysqlLockMapper;
import com.vict.framework.utils.lock.entity.MysqlLock;
import com.vict.framework.web.ApiPrePath;
import com.vict.service.TestService;
import com.vict.utils.KeyValueUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@ApiPrePath
@Api(value="测试接口", tags={"测试接口"}, hidden = true)
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    private static final String SEAGGER_TAGS = "测试接口-七牛测试";

    @Autowired
    private MysqlLockMapper mysqlLockMapper;

    @Autowired
    private TestService testService;

    @ApiOperation(tags = SEAGGER_TAGS, value = "test", httpMethod = "POST")
    @PostMapping("/test")
    public R test(){
        throw new RuntimeException("参数异常");
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "keyValueSet", httpMethod = "POST")
    @PostMapping("/keyValueSet")
    public R keyValueSet(){
        KeyValue keyValue = new KeyValue();
        keyValue.setKey("111");
        keyValue.setValue1("111222");
        KeyValueUtil.addOrUpdateByKey(keyValue);
        return R.ok();
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "keyValueGet", httpMethod = "POST")
    @PostMapping("/keyValueGet")
    public R<KeyValue> keyValueGet(){
        KeyValue valueByKey = KeyValueUtil.getValueByKey("111");
        return R.ok(valueByKey);
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "task1", httpMethod = "POST")
    @PostMapping("/task1")
    public R task1(){
        TaskExecuteUtil.sendTask("testTask", "222");
        return R.ok();
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "task2", httpMethod = "POST")
    @PostMapping("/task2")
    public R task2(){
        testService.testSimpleTask();
        return R.ok();
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "task3", httpMethod = "POST")
    @PostMapping("/task3")
    public R task3(){
        String requestId = UserContextUtil.getContext().getRequestId();
        log.info("requestId:" + requestId);
        SetTimeoutDto setTimeoutDto = new SetTimeoutDto();
        setTimeoutDto.setTime(new Timestamp(System.currentTimeMillis() + 5000L));
        testService.testSetTimeout(setTimeoutDto);
        return R.ok();
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "task4", httpMethod = "POST")
    @PostMapping("/task4")
    public R<BuserVO> task4(){
        Buser buser = new Buser();
        buser.setUsername("测试");
        BuserVO buserVO = new BuserVO();
        buserVO.setBuser(buser);
        return R.ok(buserVO);
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "MysqlLockTest", httpMethod = "POST")
    @PostMapping("/MysqlLockTest")
    public R MysqlLockTest(){
        MysqlLock mysqlLock = new MysqlLock();
        mysqlLock.setLockkey("test");
        mysqlLock.setCreated(new Timestamp(System.currentTimeMillis()));
        mysqlLock.setOvertime(new Timestamp(System.currentTimeMillis()));
        int insert = mysqlLockMapper.insert(mysqlLock);
        System.out.println(insert);
        return R.ok();
    }

    @SneakyThrows
    @ApiOperation(tags = SEAGGER_TAGS, value = "MysqlLockTestLock1", httpMethod = "POST")
    @PostMapping("/MysqlLockTestLock1")
    public R MysqlLockTestLock1(){
        LockMysql lock = LockMysql.getLock("test111");
        if(lock.lockNotBlock()){
            try{
                Thread.sleep(10000L);
                System.out.println("111111111");
            }finally{
                lock.unlockIfSuccess();
            }
        }

        return R.ok();
    }

    @SneakyThrows
    @ApiOperation(tags = SEAGGER_TAGS, value = "MysqlLockTestLock2", httpMethod = "POST")
    @PostMapping("/MysqlLockTestLock2")
    public R MysqlLockTestLock2(){
        LockMysql lock = LockMysql.getLock("test111");
        if(lock.lockNotBlock()){
            try{
                Thread.sleep(10000L);
                System.out.println("2222222222");
            }finally{
                lock.unlockIfSuccess();
            }
        }
        return R.ok();
    }
}
