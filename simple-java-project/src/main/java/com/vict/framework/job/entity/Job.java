package com.vict.framework.job.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.vict.framework.fastjsonserializer.EnumDeserializer;
import com.vict.framework.fastjsonserializer.EnumSerializer;
import com.vict.framework.fastjsonserializer.LongDeserializer;
import com.vict.framework.fastjsonserializer.LongSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@ApiModel
@TableName("job")
public class Job {

    @TableId
    @ApiModelProperty("id")
    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    private Long id;

    @ApiModelProperty("名称")
    @TableField("job_name")
    private String jobName;

    @ApiModelProperty("名称")
    @TableField("cron")
    private String cron;

    @ApiModelProperty("名称")
    @TableField("bean_name")
    private String beanName;

    @ApiModelProperty("名称")
    @TableField("method_name")
    private String methodName;

    @JSONField(serializeUsing = EnumSerializer.class, deserializeUsing = EnumDeserializer.class)
    @TableField("status")
    private Status status;

    @Getter
    @AllArgsConstructor
    public enum Status implements IEnum<String> {
        enable("enable", "启用"),
        disabled("disabled", "禁用"),
        ;
        private String value;
        private String name;

        @Override
        public String toString() {
            return "Status{" +
                    "value='" + value + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

}
