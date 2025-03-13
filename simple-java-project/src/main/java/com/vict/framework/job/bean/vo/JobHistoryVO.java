package com.vict.framework.job.bean.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.vict.framework.fastjsonserializer.*;
import com.vict.framework.job.entity.JobHistory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Optional;

@Data
public class JobHistoryVO {

    @JSONField(serialize = false, deserialize = false)
    private JobHistory jobHistory;

    @ApiModelProperty("id")
    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    public Long getId(){
        return Optional.ofNullable(jobHistory).map(o-> o.getId()).orElse(null);
    }

    @JSONField(serializeUsing = TimestampSerializer.class, deserializeUsing = TimestampDeserializer.class)
    public Timestamp getRunTime(){
        return Optional.ofNullable(jobHistory).map(o-> o.getRunTime()).orElse(null);
    }

    @JSONField(serializeUsing = EnumSerializer.class, deserializeUsing = EnumDeserializer.class)
    public JobHistory.Status getStatus(){
        return Optional.ofNullable(jobHistory).map(o-> o.getStatus()).orElse(null);
    }

    @JSONField(serializeUsing = TimestampSerializer.class, deserializeUsing = TimestampDeserializer.class)
    public Timestamp getStopTime(){
        return Optional.ofNullable(jobHistory).map(o-> o.getStopTime()).orElse(null);
    }

    public String getException(){
        return Optional.ofNullable(jobHistory).map(o-> o.getException()).orElse(null);
    }
}
