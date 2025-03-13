package com.vict.controller.test;

import com.vict.framework.bean.R;
import com.vict.framework.utils.cache.CacheUtils;
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
public class CacheTestController {

    private static final String SEAGGER_TAGS = "测试接口-缓存测试";

    @ApiOperation(tags = SEAGGER_TAGS, value = "添加缓存", notes = "添加缓存", position = 50)
    @PostMapping("/addCache")
    public R addCache(@RequestParam("key")String key,
                      @RequestParam("value")String value,
                      @RequestParam(value = "timeMillSeconds", required = false)Long timeMillSeconds){
        CacheUtils.addCache(key, value, timeMillSeconds);
        return R.ok();
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "查询缓存", notes = "查询缓存", position = 51)
    @PostMapping("/selectCache")
    public R<String> selectCache(@RequestParam("key")String key){
        String value = CacheUtils.selectCache(key);
        return R.ok(value);
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "删除缓存", notes = "删除缓存", position = 52)
    @PostMapping("/deleteCache")
    public R<String> deleteCache(@RequestParam("key")String key){
        CacheUtils.deleteCache(key);
        return R.ok();
    }
}
