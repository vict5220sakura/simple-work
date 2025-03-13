package com.vict.framework.settimeout.dto;

import com.vict.framework.task.bean.dto.TaskConsumeResult;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class SetTimeoutDto {

    public SetTimeoutDto(){}
    public SetTimeoutDto(Timestamp time){
        this.time = time;
    }

    /** 执行时间 */
    private Timestamp time;

    /** 代理标记 */
    private Boolean invokeFlag;

    private TaskConsumeResult taskConsumeResult;

    public void reconsumeLaterConsumer(){
        TaskConsumeResult taskConsumeResult = new TaskConsumeResult();
        taskConsumeResult.setResult(TaskConsumeResult.result_reconsumeLaterConsumer);
        this.setTaskConsumeResult(taskConsumeResult);
    }

}
