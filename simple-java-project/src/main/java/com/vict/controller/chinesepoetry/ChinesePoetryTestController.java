package com.vict.controller.chinesepoetry;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.vict.bean.buser.vo.BuserVO;
import com.vict.controller.chinesepoetry.ChinesePoetryController;
import com.vict.entity.Buser;
import com.vict.entity.chinesepoetry.ChinesePoetry;
import com.vict.entity.chinesepoetry.ChinesePoetryClassify;
import com.vict.framework.bean.R;
import com.vict.framework.web.ApiPrePath;
import com.vict.mapper.chinesepoetry.ChinesePoetryClassifyMapper;
import com.vict.mapper.chinesepoetry.ChinesePoetryMapper;
import com.vict.mapperservice.chinesepoetry.ChinesePoetryMapperService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@ApiPrePath
@Api
@Slf4j
@RestController
@RequestMapping("/test")
public class ChinesePoetryTestController {

    private static final String SEAGGER_TAGS = "古诗-测试接口";

    @Autowired
    private ChinesePoetryClassifyMapper chinesePoetryClassifyMapper;

    @Autowired
    private ChinesePoetryMapper chinesePoetryMapper;

    @Autowired
    private ChinesePoetryController chinesePoetryController;

    @ApiOperation(tags = SEAGGER_TAGS, value = "导入唐诗", httpMethod = "POST")
    @PostMapping("/importTangshi")
    public R importTangshi(){
        if (1 > 0) throw new RuntimeException("关闭该接口");
        ChinesePoetryClassify classiffy = chinesePoetryClassifyMapper.selectOne(
                new LambdaQueryWrapper<ChinesePoetryClassify>().eq(ChinesePoetryClassify::getClassifyName, "御定全唐詩")
        );

        chinesePoetryMapper.delete(new LambdaUpdateWrapper<ChinesePoetry>().eq(ChinesePoetry::getClassifyId, classiffy.getId()));

        String path = "D:\\download\\中文诗歌全集\\chinese-poetry-master\\御定全唐詩\\json";
        File[] ls = FileUtil.ls(path);
        for(File file : ls){
            try{
                String filestr = FileUtil.readString(file.getAbsolutePath(), "utf-8");
                chinesePoetryController.importDataByStr(filestr, classiffy.getId());
            }catch (Exception e){
                log.error("导入异常, {}", file.getAbsolutePath(), e);
            }
        }
        return R.ok();
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "古诗类转简体", httpMethod = "POST")
    @PostMapping("/classify2simple")
    public R classify2simple(){


        return R.ok();
    }
}
