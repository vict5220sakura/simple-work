package com.vict.controller.chinesepoetry;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.vict.bean.chinesepoetry.ao.*;
import com.vict.bean.chinesepoetry.vo.ChinesePoetryClassifyVO;
import com.vict.bean.customer.ao.DeleteCustomerAO;
import com.vict.bean.customer.ao.InsertCustomerAO;
import com.vict.bean.customer.vo.CustomerItemVO;
import com.vict.entity.Customer;
import com.vict.entity.chinesepoetry.ChinesePoetry;
import com.vict.entity.chinesepoetry.ChinesePoetryClassify;
import com.vict.framework.bean.MyPageInfo;
import com.vict.framework.bean.R;
import com.vict.framework.utils.IdUtils;
import com.vict.framework.utils.lock.LockApi;
import com.vict.framework.web.ApiPrePath;
import com.vict.mapper.chinesepoetry.ChinesePoetryClassifyMapper;
import com.vict.mapper.chinesepoetry.ChinesePoetryMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/chinesePoetryClassify")
public class ChinesePoetryClassifyController {

    private static final String SEAGGER_TAGS = "古诗分类";

    @Autowired
    private ChinesePoetryClassifyMapper chinesePoetryClassifyMapper;

    @Autowired
    private ChinesePoetryMapper chinesePoetryMapper;

    @ApiOperation(tags = SEAGGER_TAGS, value = "新增分类", httpMethod = "POST")
    @PostMapping("/insertChinesePoetryClassify")
    public R<String> insertChinesePoetryClassify(@RequestBody InsertChinesePoetryClassifyAO insertChinesePoetryClassifyAO) {
        insertChinesePoetryClassifyAO.check();

        String classifyName = insertChinesePoetryClassifyAO.getClassifyName().trim();

        LockApi lock = LockApi.getLock("insertChinesePoetryClassify");
        try{
            lock.lockBlock();
            ChinesePoetryClassify chinesePoetryClassifyOld = chinesePoetryClassifyMapper.selectOne(
                    new LambdaQueryWrapper<ChinesePoetryClassify>().eq(ChinesePoetryClassify::getClassifyName, classifyName)
            );
            if(chinesePoetryClassifyOld != null){
                throw new RuntimeException("分类名称已存在");
            }

            ChinesePoetryClassify chinesePoetryClassify = new ChinesePoetryClassify();
            chinesePoetryClassify.setId(IdUtils.snowflakeId());
            chinesePoetryClassify.setClassifyName(classifyName);

            chinesePoetryClassifyMapper.insert(chinesePoetryClassify);

            return R.ok(chinesePoetryClassify.getId().toString());
        }finally{
            lock.unlockIfSuccess();
        }
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "全部分类", httpMethod = "POST")
    @PostMapping("/allChinesePoetryClassify")
    public R<List<ChinesePoetryClassifyVO>> allChinesePoetryClassify(){
        List<ChinesePoetryClassify> chinesePoetryClassifies = chinesePoetryClassifyMapper.selectList(
                new LambdaQueryWrapper<ChinesePoetryClassify>()
                        .orderByAsc(ChinesePoetryClassify::getOrderNum)
        );

        List<ChinesePoetryClassifyVO> list = new ArrayList<>();

        for(ChinesePoetryClassify chinesePoetryClassify : chinesePoetryClassifies){
            ChinesePoetryClassifyVO chinesePoetryClassifyVO = new ChinesePoetryClassifyVO();
            chinesePoetryClassifyVO.setChinesePoetryClassify(chinesePoetryClassify);

            list.add(chinesePoetryClassifyVO);
        }

        // list.sort((o1, o2) -> {
        //     if(o1.getClassifyName().equals("其他") && !o2.getClassifyName().equals("其他")){
        //         return 1;
        //     }else if(!o1.getClassifyName().equals("其他") && o2.getClassifyName().equals("其他")){
        //         return -1;
        //     }else{
        //         return 0;
        //     }
        // });

        return R.ok(list);
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "删除分类", httpMethod = "POST")
    @PostMapping("/deleteChinesePoetryClassify")
    public R deleteChinesePoetryClassify(@RequestBody DeleteChinesePoetryClassifyAO deleteChinesePoetryClassifyAO){
        deleteChinesePoetryClassifyAO.check();

        ChinesePoetryClassify chinesePoetryClassify = chinesePoetryClassifyMapper.selectById(deleteChinesePoetryClassifyAO.getId());
        if(chinesePoetryClassify.getClassifyName().equals("其他")){
            throw new RuntimeException("其他类目不可删除");
        }

        ChinesePoetryClassify chinesePoetryClassifyOther = chinesePoetryClassifyMapper.selectOne(
                new LambdaQueryWrapper<ChinesePoetryClassify>()
                        .eq(ChinesePoetryClassify::getClassifyName, "其他")
        );

        chinesePoetryClassifyMapper.deleteById(deleteChinesePoetryClassifyAO.getId());

        if(chinesePoetryClassifyOther != null){
            chinesePoetryMapper.moveClassify(deleteChinesePoetryClassifyAO.getId(), chinesePoetryClassifyOther.getId());
        }

        return R.ok();
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "修改分类", httpMethod = "POST")
    @PostMapping("/updateChinesePoetryClassify")
    public R updateChinesePoetryClassify(@RequestBody UpdateChinesePoetryClassifyAO updateChinesePoetryClassifyAO){
        Long id = Optional.ofNullable(updateChinesePoetryClassifyAO).map(o -> o.getId()).orElse(null);
        String classifyName = Optional.ofNullable(updateChinesePoetryClassifyAO).map(o -> o.getClassifyName()).map(o-> o.trim()).orElse(null);

        ChinesePoetryClassify chinesePoetryClassify = chinesePoetryClassifyMapper.selectById(id);
        chinesePoetryClassify.setClassifyName(classifyName);

        chinesePoetryClassifyMapper.updateById(chinesePoetryClassify);

        return R.ok();
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "移动分类", httpMethod = "POST")
    @PostMapping("/move")
    public R move(@RequestBody ClassifyMoveAO classifyMoveAO){
        classifyMoveAO.check();

        Long id = classifyMoveAO.getId();

        ChinesePoetryClassify chinesePoetryClassify = chinesePoetryClassifyMapper.selectById(id);

        if (classifyMoveAO.getDirection().equals("up")){

            ChinesePoetryClassify chinesePoetryClassifyTemp = chinesePoetryClassifyMapper.selectUp(chinesePoetryClassify.getOrderNum());

            if(chinesePoetryClassifyTemp == null){
                throw new RuntimeException("没有上一条");
            }

            chinesePoetryClassifyMapper.setOrderNum(chinesePoetryClassify.getId(), chinesePoetryClassifyTemp.getOrderNum());
            chinesePoetryClassifyMapper.setOrderNum(chinesePoetryClassifyTemp.getId(), chinesePoetryClassify.getOrderNum());

        }else if(classifyMoveAO.getDirection().equals("down")){

            ChinesePoetryClassify chinesePoetryClassifyTemp = chinesePoetryClassifyMapper.selectDown(chinesePoetryClassify.getOrderNum());

            if(chinesePoetryClassifyTemp == null){
                throw new RuntimeException("没有下一条");
            }

            chinesePoetryClassifyMapper.setOrderNum(chinesePoetryClassify.getId(), chinesePoetryClassifyTemp.getOrderNum());
            chinesePoetryClassifyMapper.setOrderNum(chinesePoetryClassifyTemp.getId(), chinesePoetryClassify.getOrderNum());
        }

        return R.ok();
    }
}
