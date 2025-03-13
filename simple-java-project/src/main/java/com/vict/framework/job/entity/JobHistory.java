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

import java.sql.Timestamp;

@Data
@ApiModel
@TableName("job_history")
public class JobHistory {

    @TableId
    @ApiModelProperty("id")
    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    private Long id;

    @TableField("job_id")
    @ApiModelProperty("job_id")
    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    private Long jobId;

    @TableField("run_time")
    @ApiModelProperty("启动时间")
    private Timestamp runTime;

    @Getter
    @AllArgsConstructor
    public enum Status implements IEnum<String> {
        ok("ok", "运行成功"),
        ex("ex", "运行异常"),
        run("run", "运行中");

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

    @JSONField(serializeUsing = EnumSerializer.class, deserializeUsing = EnumDeserializer.class)
    @TableField("status")
    private Status status;

    @TableField("stop_time")
    @ApiModelProperty("停止时间")
    private Timestamp stopTime;

    @TableField("exception")
    @ApiModelProperty("异常信息")
    private String exception;

}
