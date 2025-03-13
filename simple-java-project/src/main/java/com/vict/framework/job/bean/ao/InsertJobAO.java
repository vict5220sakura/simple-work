package com.vict.framework.job.bean.ao;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.vict.framework.fastjsonserializer.EnumDeserializer;
import com.vict.framework.fastjsonserializer.EnumSerializer;
import com.vict.framework.job.entity.Job;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class InsertJobAO {
    @ApiModelProperty("名称")
    private String jobName;

    @ApiModelProperty("名称")
    private String cron;

    @ApiModelProperty("名称")
    private String beanName;

    @ApiModelProperty("名称")
    private String methodName;

    // @JSONField(serializeUsing = EnumSerializer.class, deserializeUsing = EnumDeserializer.class)
    // private Job.Status status;

    public void check() {
        if (jobName == null || jobName.trim().length() == 0) {
            throw new RuntimeException("jobName不能为空");
        }
        if (cron == null || cron.trim().length() == 0) {
            throw new RuntimeException("cron不能为空");
        }
        if (beanName == null || beanName.trim().length() == 0) {
            throw new RuntimeException("beanName不能为空");
        }
        if (methodName == null || methodName.trim().length() == 0) {
            throw new RuntimeException("methodName不能为空");
        }
        // if (status == null) {
        //     throw new RuntimeException("status不能为空");
        // }
    }
}
