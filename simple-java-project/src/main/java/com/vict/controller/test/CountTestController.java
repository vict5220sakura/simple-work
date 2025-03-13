package com.vict.controller.test;

import com.vict.framework.bean.R;
import com.vict.framework.utils.count.CountUtil;
import com.vict.framework.web.ApiPrePath;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@ApiPrePath
@Api(value="测试接口", tags={"测试接口"}, hidden = true)
@Slf4j
@RestController
@RequestMapping("/test")
public class CountTestController {

    private static final String SEAGGER_TAGS = "测试接口-计数器测试";

    @ApiOperation(tags = SEAGGER_TAGS, value = "selectNum", notes = "selectNum", position = 60)
    @PostMapping("/selectNum")
    public R<Long> selectNum(@RequestParam("key")String key){
        long num = CountUtil.selectNum(key);
        return R.ok(num);
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "nextNum", notes = "nextNum", position = 61)
    @PostMapping("/nextNum")
    public R<Long> nextNum(@RequestParam("key")String key){
        long num = CountUtil.nextNum(key);
        return R.ok(num);
    }


}
