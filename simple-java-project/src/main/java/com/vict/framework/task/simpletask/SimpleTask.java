package com.vict.framework.task.simpletask;

import com.vict.framework.task.annotation.TaskConsumerConfiguration;
import com.vict.framework.task.bean.dto.*;
import com.vict.framework.task.core.TaskExecute;
import com.vict.framework.task.simpletask.bean.SimpleTaskDto;
import com.vict.framework.task.simpletask.bean.SimpleTaskParamDto;
import com.vict.framework.task.simpletask.bean.SimpleTaskParamDtoList;
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
@TaskConsumerConfiguration(type = SimpleTask.type, sameTimeWorkNum = 50)
public class SimpleTask implements TaskExecute<SimpleTaskDto> {
    public static final String type = "SimpleTask";

    @SneakyThrows
    @Override
    public TaskConsumeResult doConsume(Task task, SimpleTaskDto localSimpleTaskDto) {
        String className = localSimpleTaskDto.getClassName();
        String methodName = localSimpleTaskDto.getMethodName();
        SimpleTaskParamDtoList paramDtoList = localSimpleTaskDto.getArgs();

        Object springBeanService = SpringUtils.getBean(Class.forName(className));
        Method method = Arrays.stream(springBeanService.getClass().getMethods()).filter(o -> o.getName().equals(methodName)).collect(Collectors.toList()).get(0);

        List<SimpleTaskParamDto> list = paramDtoList.getList();
        Object[] args = new Object[list.size()];
        for(int i = 0 ; i < list.size() ; i++){
            SimpleTaskParamDto paramDto = list.get(i);
            String paramClassName = paramDto.getClassName();
            Object obj = paramDto.getObj();
            args[i] = obj;
        }

        try{
            ThreadUtil.putThreadVariable("TaskInvokeTrueFlag", "TaskInvokeTrueFlag");
            method.invoke(springBeanService, args);
            ThreadUtil.removeThreadVariable("TaskInvokeTrueFlag");
            return task.commitConsumer();
        }catch(Exception e){
            log.error("simpleTask服务异常", e);
            return task.reconsumeLaterConsumer();
        }
    }
}
