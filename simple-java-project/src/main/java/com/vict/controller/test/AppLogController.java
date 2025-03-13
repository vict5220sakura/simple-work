package com.vict.controller.test;

import com.vict.bean.app.AppLogInfoAO;
import com.vict.bean.chinesepoetry.ao.InsertChinesePoetryClassifyAO;
import com.vict.bean.ws.dto.MessageDTO;
import com.vict.framework.bean.R;
import com.vict.framework.web.ApiPrePath;
import com.vict.ws.BuserWebSocketServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApiPrePath
@Api
@Slf4j
@RestController
@RequestMapping("/applog")
public class AppLogController {
    private static final String SEAGGER_TAGS = "app日志";

    @ApiOperation(tags = SEAGGER_TAGS, value = "test", httpMethod = "POST")
    @PostMapping("/info")
    public R info(@RequestBody AppLogInfoAO appLogInfoAO){

        List<String> strings = Optional.ofNullable(appLogInfoAO).map(o -> o.getLogInfos()).orElse(new ArrayList<String>());

        String join = String.join(",", strings.toArray(new String[strings.size()]));


        log.info("【app日志】" + join);

        return R.ok();
    }
}
