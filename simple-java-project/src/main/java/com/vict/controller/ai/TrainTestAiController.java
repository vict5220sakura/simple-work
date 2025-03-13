package com.vict.controller.ai;

import com.vict.bean.ai.ao.TrainAiChatAO;
import com.vict.bean.ai.dto.TrainAiDTO;
import com.vict.bean.ai.vo.TrainAiChatVO;
import com.vict.framework.bean.R;
import com.vict.framework.utils.cache.CacheUtils;
import com.vict.framework.web.ApiPrePath;
import com.vict.service.ai.TrainTestAiService;
import com.vict.utils.TimeUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@ApiPrePath
@Api
@Slf4j
@RestController
@RequestMapping("/trainAi")
public class TrainTestAiController {

    @Autowired
    private TrainTestAiService trainTestAiService;

    @PostMapping("/chat")
    public R<TrainAiChatVO> addUser(@RequestBody TrainAiChatAO trainAiChatAO){
        TrainAiDTO trainAiDTO = CacheUtils.selectCache("aiTrainTest_" + trainAiChatAO.getId(), TrainAiDTO.class);
        if(trainAiDTO == null){
            trainAiDTO = new TrainAiDTO();
            CacheUtils.addCache("aiTrainTest_" + trainAiChatAO.getId(), trainAiDTO);

            TrainAiChatVO trainAiChatVO = new TrainAiChatVO();
            trainAiChatVO.setMessage("欢迎使用订票智能 AI，请告诉我你的需求。");
            return R.ok(trainAiChatVO);
        }

        // 解析
        if(trainAiDTO.getUserIntentEnum() == null
                || trainAiDTO.getUserIntentEnum() == TrainTestAiService.UserIntentEnum.userIntentEnum_other){ // 用户意图
            TrainTestAiService.UserIntentEnum userIntentEnum = trainTestAiService.userIntentAnalysis(trainAiChatAO.getMessage());
            trainAiDTO.setUserIntentEnum(userIntentEnum);
        }
        if(trainAiDTO.getStartTimestamp() == null){
            Long time = trainTestAiService.analysisStartTimestamp(trainAiChatAO.getMessage());
            trainAiDTO.setStartTimestamp(time);
        }
        if(trainAiDTO.getFrom() == null || trainAiDTO.getTo() == null){
            TrainTestAiService.FromTo fromTo = trainTestAiService.analysisFromTo(trainAiChatAO.getMessage());
            trainAiDTO.setFrom(Optional.ofNullable(fromTo).map(o-> o.getFrom()).orElse(null));
            trainAiDTO.setTo(Optional.ofNullable(fromTo).map(o-> o.getTo()).orElse(null));
        }

        // 问题+解析
        if(trainAiChatAO.getQuestion().equals("请问您从哪出发?") && trainAiDTO.getFrom() == null){
            String from = trainTestAiService.analysisAddress(trainAiChatAO.getMessage());
            trainAiDTO.setFrom(Optional.ofNullable(from).orElse(null));
        }
        if(trainAiChatAO.getQuestion().equals("请问您出发去哪?") && trainAiDTO.getTo() == null){
            String to = trainTestAiService.analysisAddress(trainAiChatAO.getMessage());
            trainAiDTO.setTo(Optional.ofNullable(to).orElse(null));
        }

        // 存储解析信息
        CacheUtils.addCache("aiTrainTest_" + trainAiChatAO.getId(), trainAiDTO);

        // 响应
        // 其他需求处理
        if(trainAiDTO.getUserIntentEnum() == TrainTestAiService.UserIntentEnum.userIntentEnum_other){
            TrainAiChatVO trainAiChatVO = new TrainAiChatVO();
            trainAiChatVO.setMessage("欢迎使用订票智能 AI，请告诉我你的订票需求。其他需求暂时无法满足");
            return R.ok(trainAiChatVO);
        }

        if(trainAiDTO.getUserIntentEnum() == TrainTestAiService.UserIntentEnum.userIntentEnum_train){

            if(trainAiDTO.getStartTimestamp() == null) {
                TrainAiChatVO trainAiChatVO = new TrainAiChatVO();
                trainAiChatVO.setMessage("请问您需要几号出行?");
                return R.ok(trainAiChatVO);
            }

            if(trainAiDTO.getFrom() == null){
                TrainAiChatVO trainAiChatVO = new TrainAiChatVO();
                trainAiChatVO.setMessage("请问您从哪出发?");
                return R.ok(trainAiChatVO);
            }

            if(trainAiDTO.getTo() == null){
                TrainAiChatVO trainAiChatVO = new TrainAiChatVO();
                trainAiChatVO.setMessage("请问您出发去哪?");
                return R.ok(trainAiChatVO);
            }

            // 订票成功
            String timestr = TimeUtil.getTimeStr("yyyy年MM月dd日", trainAiDTO.getStartTimestamp());
            String from = trainAiDTO.getFrom();
            String to = trainAiDTO.getTo();

            TrainAiChatVO trainAiChatVO = new TrainAiChatVO();
            trainAiChatVO.setMessage(
                    String.format("已为您定好 %s, %s 到 %s 的火车票", timestr, from, to)
            );

            CacheUtils.deleteCache("aiTrainTest_" + trainAiChatAO.getId());

            return R.ok(trainAiChatVO);
        }

        if(trainAiDTO.getUserIntentEnum() == TrainTestAiService.UserIntentEnum.userIntentEnum_hotel){
            TrainAiChatVO trainAiChatVO = new TrainAiChatVO();
            trainAiChatVO.setMessage("服务器繁忙, 请稍后再试");
            return R.ok(trainAiChatVO);
        }

        TrainAiChatVO trainAiChatVO = new TrainAiChatVO();
        trainAiChatVO.setMessage("服务器繁忙, 请稍后再试");
        return R.ok(trainAiChatVO);
    }
}
