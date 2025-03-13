package com.vict.framework.keyvalue.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.vict.framework.keyvalue.entity.KeyValue;
import com.vict.framework.keyvalue.bean.ao.DeleteKeyValueByIdAO;
import com.vict.framework.keyvalue.bean.ao.KeyValueListAO;
import com.vict.framework.keyvalue.bean.vo.keyValueVO;
import com.vict.framework.Constants;
import com.vict.framework.FrameworkCommon;
import com.vict.framework.bean.MyPageInfo;
import com.vict.framework.bean.R;
import com.vict.framework.utils.UserContextUtil;
import com.vict.framework.utils.lock.LockApi;
import com.vict.framework.web.ApiPrePath;
import com.vict.framework.keyvalue.mapper.KeyValueMapper;
import com.vict.framework.keyvalue.service.KeyValueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 */
@ApiPrePath
@Api
@Slf4j
@RestController
@RequestMapping("/keyValue")
public class KeyValueController {

    @Autowired
    private KeyValueService keyValueService;

    @Autowired
    private KeyValueMapper keyValueMapper;

    @ApiOperation(value = "新增字典数据", notes = "新增字典数据",position = 10)
    @PostMapping("/insertKeyValue")
    public R insertKeyValue(@RequestBody KeyValue request) {
        LockApi lock = LockApi.getLock("keyValue_key-" + request.getKey());
        try{
            lock.lockBlock();
            KeyValue keyValue = keyValueMapper.selectOne(new LambdaQueryWrapper<KeyValue>().eq(KeyValue::getKey, request.getKey()));
            if(keyValue != null){
                throw new RuntimeException("key重复, 请检查!");
            }
            keyValueService.insertKeyValue(request);
        }finally{
            lock.unlockIfSuccess();
        }

        return R.ok();
    }

    @ApiOperation(value = "删除字典数据", notes = "删除字典数据",position = 20)
    @PostMapping("/deleteKeyValueById")
    public R deleteKeyValueById(@RequestBody DeleteKeyValueByIdAO deleteKeyValueByIdAO) {
        keyValueService.deleteKeyValueById(deleteKeyValueByIdAO.getId());
        return R.ok();
    }

    @ApiOperation(value = "修改字典数据", notes = "修改字典数据",position = 30)
    @PostMapping("/updateKeyValueById")
    public R updateKeyValueById(@RequestBody KeyValue request) {
        keyValueService.updateKeyValueById(request);


        // try{ // 手动触发定时任务
        //     if(request.getKey().equals("hand_action_job")){
        //         KeyValue jobKeyValue = KeyValueUtil.getValueByKey(request.getValue1());
        //         JSONObject jsonObject = JSONObject.parseObject(jobKeyValue.getValue1().trim());
        //         String type = jsonObject.getString("type");
        //         if(type != null && type.trim().equals("job")){
        //             String server = jsonObject.getString("server");
        //             String cron = jsonObject.getString("cron");
        //             String key = jobKeyValue.getKey();
        //             MyJobCore.sendHandActionJob(server, key);
        //         }
        //
        //     }
        // }catch(Exception e){
        //     log.error("", e);
        // }

        // try{ // 手动触发定时任务
        //     if(request.getKey().equals("local_hand_action_job")){
        //         String[] split = request.getValue1().trim().split(",");
        //         String key = split[0].trim();
        //         String host = split[1].trim();
        //
        //         KeyValue jobKeyValue = KeyValueUtil.getValueByKey(key);
        //
        //         JSONObject jsonObject = JSONObject.parseObject(jobKeyValue.getValue1());
        //         String type = jsonObject.getString("type");
        //         if(type != null && type.trim().equals("job")){
        //             String server = jsonObject.getString("server");
        //             MyJobCore.sendHandActionLocal(host, server, key);
        //         }
        //
        //     }
        // }catch(Exception e){
        //     log.error("", e);
        // }

        // try{ // 定时任务修改cron
        //     if(request.getValue1().trim().startsWith("{")){
        //         JSONObject jsonObject = JSONObject.parseObject(request.getValue1().trim());
        //         String type = jsonObject.getString("type");
        //         if(type != null && type.trim().equals("job")){
        //             String server = jsonObject.getString("server");
        //             String cron = jsonObject.getString("cron");
        //             String key = request.getKey();
        //             MyJobCore.sendRestartJob(server, key);
        //         }
        //
        //     }
        // }catch(Exception e){
        //     log.error("", e);
        // }

        try{
            if(request.getKey().equals("keyValue_no_auth_key")){
                keyValueService.reloadKeyValueNoAuthKey();
            }
        }catch(Exception e){
            log.error("", e);
        }

        return R.ok();
    }

    @ApiOperation(value = "获取字典数据", notes = "获取字典数据",position = 40)
    @PostMapping("/getKeyValueById")
    public R<KeyValue> getKeyValueById(@RequestParam Long id) {
        KeyValue response = keyValueService.getKeyValueById(id);
        return R.ok(response);
    }

    @ApiOperation(value = "获取字典数据列表", notes = "获取字典数据列表",position = 50)
    @PostMapping("/keyValueList")
    public R<MyPageInfo<keyValueVO>> keyValueList(@RequestBody KeyValueListAO keyValueListAO) {
        Page<KeyValue> page = PageHelper.startPage(keyValueListAO.getPageNum(), keyValueListAO.getPageSize());

        keyValueMapper.selectMyList(keyValueListAO.getKey(), keyValueListAO.getDesc(), 0);

        List<KeyValue> result = Optional.ofNullable(page).map(o -> o.getResult()).orElse(new ArrayList<>());

        MyPageInfo myPageInfo = new MyPageInfo(page);

        List<keyValueVO> keyValueVOS = new ArrayList<>();
        for(KeyValue keyValue : result){
            keyValueVO keyValueVO = new keyValueVO();
            BeanUtils.copyProperties(keyValue, keyValueVO);
            keyValueVOS.add(keyValueVO);
        }

        myPageInfo.setList(keyValueVOS);

        return R.ok(myPageInfo);
    }

    @ApiOperation(value = "获取字典数据(keyValue_no_auth_key 配置不需要登录key [\"\",\"\"])", notes = "获取字典数据列表",position = 50)
    @PostMapping("/keyValueByKey")
    public R<KeyValue> keyValueByKey(@RequestParam("key") String key) {
        if(FrameworkCommon.keyValueNoAuthKeyList.contains(key) == false){ // 不包含需要判断登录状态
            if(UserContextUtil.isLogin()){
                // 当前已登录
            }else{
                // 当前未登录
                return R.failed(Constants.authFail, Constants.authFailMsg);
            }
        }
        KeyValue keyValueByKey = keyValueService.getKeyValueByKey(key);
        return R.ok(keyValueByKey);
    }

}
