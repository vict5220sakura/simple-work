package com.vict.framework.settimeout.aspect;

import com.vict.framework.settimeout.dto.ParamDto;
import com.vict.framework.settimeout.dto.ParamDtoList;
import com.vict.framework.settimeout.dto.SetTimeoutDto;
import com.vict.framework.settimeout.dto.SetTimeoutTaskDto;
import com.vict.framework.settimeout.task.SetTimeoutTask;
import com.vict.framework.task.util.TaskExecuteUtil;
import com.vict.framework.utils.UserContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Slf4j
@Aspect
@Component
public class SetTimeoutAspect {

    @Around("@annotation(com.vict.framework.settimeout.annotation.SetTimeout)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        try{
            Object[] args = joinPoint.getArgs();
            if(args == null){
                throw new RuntimeException("SetTimeout注释的方法 必须有SetTimeoutDto参数");
            }
            SetTimeoutDto setTimeoutDto = null;
            for(Object arg : args){
                if(arg instanceof SetTimeoutDto){
                    setTimeoutDto = (SetTimeoutDto)arg;
                    break;
                }
            }
            if(setTimeoutDto == null){
                throw new RuntimeException("FutureExec注释的方法 必须有FutureExecDto参数");
            }
            if(setTimeoutDto.getTime() == null){
                throw new RuntimeException("futureExecDto.execTime不能为空");
            }

            if(setTimeoutDto.getInvokeFlag() != null && setTimeoutDto.getInvokeFlag() == true){ // 反射调用
                return joinPoint.proceed();
            }


            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            Class[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();

            // 对象参数

            // Object springBeanService = SpringUtils.getBean(Class.forName(className));
            // Method method = Arrays.stream(springBeanService.getClass().getMethods()).filter(o -> o.getName().equals(methodName)).collect(Collectors.toList()).get(0);

            ParamDtoList paramDtoList = new ParamDtoList();
            paramDtoList.setList(new ArrayList<>());
            for(int i = 0 ; i < args.length ; i++){
                String paramClassName = parameterTypes[i].getName();
                Object arg = args[i];
                ParamDto paramDto = new ParamDto();
                paramDto.setClassName(paramClassName);
                paramDto.setObj(arg);
                paramDtoList.getList().add(paramDto);
            }

            SetTimeoutTaskDto setTimeoutTaskDto = new SetTimeoutTaskDto();

            setTimeoutTaskDto.setClassName(className);
            setTimeoutTaskDto.setMethodName(methodName);
            setTimeoutTaskDto.setArgs(paramDtoList);
            setTimeoutTaskDto.setUserContext(UserContextUtil.getContext());

            TaskExecuteUtil.sendTask(SetTimeoutTask.type, setTimeoutTaskDto, setTimeoutDto.getTime());

            return null;
        }catch(Exception e){
            log.error("SetTimeout异常", e);
            throw e;
        }
    }
}
