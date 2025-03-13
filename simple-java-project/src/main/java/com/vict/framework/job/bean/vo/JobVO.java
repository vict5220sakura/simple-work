package com.vict.framework.job.bean.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.vict.framework.fastjsonserializer.*;
import com.vict.framework.job.entity.Job;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Optional;

@Data
public class JobVO {

    @JSONField(serialize = false, deserialize = false)
    private Job job;

    @ApiModelProperty("id")
    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    public Long getId(){
        return Optional.ofNullable(job).map(o-> o.getId()).orElse(null);
    }

    @ApiModelProperty("名称")
    public String getJobName(){
        return Optional.ofNullable(job).map(o-> o.getJobName()).orElse(null);
    }

    @ApiModelProperty("cron")
    public String getCron(){
        return Optional.ofNullable(job).map(o-> o.getCron()).orElse(null);
    }

    @ApiModelProperty("bean名称")
    public String getBeanName(){
        return Optional.ofNullable(job).map(o-> o.getBeanName()).orElse(null);
    }

    @ApiModelProperty("方法名称")
    public String getMethodName(){
        return Optional.ofNullable(job).map(o-> o.getMethodName()).orElse(null);
    }

    @ApiModelProperty("状态")
    @JSONField(serializeUsing = EnumSerializer.class, deserializeUsing = EnumDeserializer.class)
    public Job.Status getStatus(){
        return Optional.ofNullable(job).map(o-> o.getStatus()).orElse(null);
    }
}
