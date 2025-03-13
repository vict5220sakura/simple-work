package com.vict.bean.ai.dto;

import com.vict.service.ai.TrainTestAiService;
import lombok.Data;

@Data
public class TrainAiDTO {
    // 用户意图
    private TrainTestAiService.UserIntentEnum userIntentEnum;

    // 出行日期
    private Long startTimestamp;

    // 出发地
    private String from;
    // 到达地
    private String to;
}
