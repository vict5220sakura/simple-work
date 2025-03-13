package com.vict.framework.task.simpletask;

import com.vict.framework.task.simpletask.bean.SimpleTaskDto;
import com.vict.framework.task.simpletask.bean.SimpleTaskParamDto;
import com.vict.framework.task.simpletask.bean.SimpleTaskParamDtoList;
import com.vict.framework.task.util.TaskExecuteUtil;
import com.vict.utils.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Aspect
@Component
public class TaskAspect {
    @Around("@annotation(com.vict.framework.task.simpletask.SimpleTaskRun)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        try{
            Object[] args = joinPoint.getArgs();

            String invokeFlagStr = Optional.ofNullable(ThreadUtil.getThreadVariable("TaskInvokeTrueFlag"))
                    .map(o -> (String) o)
                    .orElse(null);

            if(invokeFlagStr != null && !invokeFlagStr.trim().equals("")){ // 反射调用 未来task调用
                ThreadUtil.removeThreadVariable("TaskInvokeTrueFlag");
                return joinPoint.proceed();
            }

            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            Class[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();

            int[] failRetryTimeSeconds = Optional.ofNullable(((MethodSignature) joinPoint.getSignature()).getMethod())
                    .map(o -> o.getAnnotation(SimpleTaskRun.class))
                    .map(SimpleTaskRun::failRetryTimeSeconds).orElse(null);

            // 对象参数

            SimpleTaskParamDtoList paramDtoList = new SimpleTaskParamDtoList();
            paramDtoList.setList(new ArrayList<>());
            for(int i = 0 ; i < args.length ; i++){
                String paramClassName = parameterTypes[i].getName();
                Object arg = args[i];
                SimpleTaskParamDto paramDto = new SimpleTaskParamDto();
                paramDto.setClassName(paramClassName);
                paramDto.setObj(arg);
                paramDtoList.getList().add(paramDto);
            }

            SimpleTaskDto setTimeoutTaskDto = new SimpleTaskDto();

            setTimeoutTaskDto.setClassName(className);
            setTimeoutTaskDto.setMethodName(methodName);
            setTimeoutTaskDto.setArgs(paramDtoList);

            TaskExecuteUtil.sendTaskRetryTime(SimpleTask.type, setTimeoutTaskDto, failRetryTimeSeconds);

            return null;
        }catch(Exception e){
            // log.error("TaskSyncAspect异常", e);
            throw e;
        }
    }
}
