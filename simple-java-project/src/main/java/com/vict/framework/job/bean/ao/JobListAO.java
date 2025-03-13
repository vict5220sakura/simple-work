package com.vict.framework.job.bean.ao;

import com.alibaba.fastjson.annotation.JSONField;
import com.vict.framework.bean.PageRequest;
import com.vict.framework.fastjsonserializer.EnumDeserializer;
import com.vict.framework.fastjsonserializer.EnumSerializer;
import com.vict.framework.job.entity.Job;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class JobListAO extends PageRequest {

    @ApiModelProperty("名称")
    private String jobName;

    @ApiModelProperty("名称")
    private String beanName;

    @ApiModelProperty("名称")
    private String methodName;

    @JSONField(serializeUsing = EnumSerializer.class, deserializeUsing = EnumDeserializer.class)
    private Job.Status status;
}
