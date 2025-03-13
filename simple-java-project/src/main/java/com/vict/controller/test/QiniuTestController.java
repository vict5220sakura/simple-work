package com.vict.controller.test;

import com.vict.framework.bean.R;
import com.vict.framework.web.ApiPrePath;
import com.vict.utils.FileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@ApiPrePath
@Api(value="七牛测试", tags={"七牛测试"}, hidden = true)
@Slf4j
@RestController
@RequestMapping("/test/qiniu")
public class QiniuTestController {
    private static final String SEAGGER_TAGS = "测试接口-七牛测试";

    // @ApiOperation(tags = SEAGGER_TAGS, value = "upload", notes = "upload", position = 60)
    // @PostMapping("/upload")
    // public R selectNum(@RequestParam("file")String file){
    //     String url = FileUtils.uploadQiNiu(file, file);
    //     return R.ok(url);
    // }
}
