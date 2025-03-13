package com.vict.framework.job.bean.ao;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.vict.framework.bean.PageRequest;
import com.vict.framework.fastjsonserializer.*;
import com.vict.framework.job.entity.JobHistory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class JobHistoryListAO extends PageRequest {

    @ApiModelProperty("id")
    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    private Long id;

    @ApiModelProperty("开始时间")
    @JSONField(serializeUsing = TimestampSerializer.class, deserializeUsing = TimestampDeserializer.class)
    private Timestamp startTime;

    @ApiModelProperty("结束时间")
    @JSONField(serializeUsing = TimestampSerializer.class, deserializeUsing = TimestampDeserializer.class)
    private Timestamp endTime;

    @JSONField(serializeUsing = EnumSerializer.class, deserializeUsing = EnumDeserializer.class)
    @TableField("status")
    private JobHistory.Status status;
}
