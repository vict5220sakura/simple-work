package com.vict.framework.task.core.impl;

import com.vict.framework.task.bean.dto.TaskSend;
import com.vict.framework.task.bean.entity.TaskExecuteInstance;
import com.vict.framework.task.core.TaskService;
import com.vict.framework.task.dao.TaskExecuteInstanceDao;
import com.vict.framework.task.util.TaskExecuteUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    TaskExecuteInstanceDao taskExecuteInstanceDao;

    @SneakyThrows
    public void commitConsumer(String taskExecuteInstanceID){
        try{

            taskExecuteInstanceDao.updateStatusById(taskExecuteInstanceID, TaskExecuteInstance.status_finish);

        }catch(Exception e){
            throw e;
        }
    }

    @SneakyThrows
    @Override
    public void commitConsumer(String taskExecuteInstanceID, List<TaskSend> sendTaskList) {
        try{
            taskExecuteInstanceDao.updateStatusById(taskExecuteInstanceID, TaskExecuteInstance.status_finish);

            if(sendTaskList != null && sendTaskList.size() > 0){
                for(TaskSend sendTask : sendTaskList){
                    TaskExecuteUtil.sendTask(sendTask.getType(), sendTask.getT(), sendTask.getActionTime());
                }
            }
        }catch(Exception e){
            throw e;
        }

    }

    @SneakyThrows
    @Override
    public void commitConsumer(String taskExecuteInstanceID, TaskSend sendTask) {
        try{
            taskExecuteInstanceDao.updateStatusById(taskExecuteInstanceID, TaskExecuteInstance.status_finish);
            if(sendTask != null){
                TaskExecuteUtil.sendTask(sendTask.getType(), sendTask.getT(), sendTask.getActionTime());
            }
        }catch(Exception e){
            throw e;
        }
    }
}
