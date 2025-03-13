package com.vict.framework.settimeout.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vict.framework.bean.UserContext;
import com.vict.framework.settimeout.dto.ParamDto;
import com.vict.framework.settimeout.dto.ParamDtoList;
import com.vict.framework.settimeout.dto.SetTimeoutDto;
import com.vict.framework.settimeout.dto.SetTimeoutTaskDto;
import com.vict.framework.task.annotation.TaskConsumerConfiguration;
import com.vict.framework.task.bean.dto.Task;
import com.vict.framework.task.bean.dto.TaskConsumeResult;
import com.vict.framework.task.core.TaskExecute;
import com.vict.utils.SpringUtils;
import com.vict.utils.ThreadUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@TaskConsumerConfiguration(type = SetTimeoutTask.type, sameTimeWorkNum = 200)
public class SetTimeoutTask implements TaskExecute<SetTimeoutTaskDto> {

    public static final String type = "setTimeout";

    @SneakyThrows
    @Override
    public TaskConsumeResult doConsume(Task task, SetTimeoutTaskDto setTimeoutTaskDto) {
        UserContext userContext = Optional.ofNullable(setTimeoutTaskDto).map(o -> o.getUserContext()).orElse(null);
        if(userContext != null){
            ThreadUtil.putThreadVariable("userContext", userContext);
        }

        String className = setTimeoutTaskDto.getClassName();
        String methodName = setTimeoutTaskDto.getMethodName();
        ParamDtoList paramDtoList = setTimeoutTaskDto.getArgs();

        Object springBeanService = SpringUtils.getBean(Class.forName(className));
        Method method = Arrays.stream(springBeanService.getClass().getMethods()).filter(o -> o.getName().equals(methodName)).collect(Collectors.toList()).get(0);

        List<ParamDto> list = paramDtoList.getList();
        Object[] args = new Object[list.size()];
        for(int i = 0 ; i < list.size() ; i++){
            ParamDto paramDto = list.get(i);
            String paramClassName = paramDto.getClassName();
            Object obj = paramDto.getObj();

            try{
                Class<?> aClass = Class.forName(paramClassName);
                Object o1 = JSONObject.toJavaObject((JSON) JSONObject.toJSON(obj), aClass);
                args[i] = o1;
            }catch(Exception e){
                log.error("序列化异常 为常量json数据");
                args[i] = obj;
            }
        }

        SetTimeoutDto setTimeoutDto = null;
        for(Object arg : args){
            if(arg instanceof SetTimeoutDto){
                setTimeoutDto = (SetTimeoutDto)arg;
                break;
            }
        }
        if(setTimeoutDto == null){
            throw new RuntimeException("PayAwait注释的方法 必须有PayDTO参数");
        }

        try{
            setTimeoutDto.setInvokeFlag(true);

            method.invoke(springBeanService, args);

            Integer result = Optional.ofNullable(setTimeoutDto).map(o -> o.getTaskConsumeResult()).map(o -> o.getResult()).orElse(null);
            if(result != null && result == TaskConsumeResult.result_reconsumeLaterConsumer){
                return task.reconsumeLaterConsumer();
            }

            return task.commitConsumer();
        }catch(Exception e){
            log.error("期限服务异常", e);
            Integer result = Optional.ofNullable(setTimeoutDto).map(o -> o.getTaskConsumeResult()).map(o -> o.getResult()).orElse(null);
            if(result != null && result == TaskConsumeResult.result_reconsumeLaterConsumer){
                return task.reconsumeLaterConsumer();
            }
            return task.failConsumer();
        }
    }
}
