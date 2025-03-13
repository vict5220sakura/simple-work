package com.vict.controller.custompage;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.vict.bean.customepage.ao.*;
import com.vict.bean.customepage.vo.CustomPageVO;
import com.vict.entity.CustomPage;
import com.vict.framework.bean.MyPageInfo;
import com.vict.framework.bean.R;
import com.vict.framework.utils.IdUtils;
import com.vict.framework.web.ApiPrePath;
import com.vict.mapper.CustomPageMapper;
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
@RequestMapping("/customPage")
public class CustomPageController {

    @Autowired
    private CustomPageMapper customPageMapper;

    @ApiOperation(value = "新增自定义页面", notes = "新增自定义页面")
    @PostMapping("/insertCustomPage")
    public R<String> insertCustomPage(@RequestBody InsertCustomPageAO insertCustomPageAO) {
        insertCustomPageAO.check();

        CustomPage customPage = new CustomPage();
        customPage.setId(IdUtils.snowflakeId());
        customPage.setCustomName(insertCustomPageAO.getCustomName());
        customPage.setPageValue(insertCustomPageAO.getPageValue());
        customPageMapper.insert(customPage);

        return R.ok();
    }

    @ApiOperation(value = "查询自定义页面", notes = "查询自定义页面")
    @PostMapping("/customPageList")
    public R<MyPageInfo<CustomPageVO>> insertCustomer(@RequestBody CustomPageListAO customPageListAO) {
        String customName = Optional.ofNullable(customPageListAO).map(o -> o.getCustomName()).map(o -> o.trim())
                .filter(o -> !o.equals("")).orElse(null);

        // 数据查询
        Page page = PageHelper.startPage(customPageListAO.getPageNum(), customPageListAO.getPageSize());

        customPageMapper.selectMyList(customName);

        List<CustomPage> customPages = Optional.ofNullable(page.getResult()).orElse(new ArrayList<>());

        MyPageInfo<CustomPageVO> myPageInfo = new MyPageInfo<CustomPageVO>(page);
        List<CustomPageVO> customPageVOS = new ArrayList<CustomPageVO>(customPages.size());
        myPageInfo.setList(customPageVOS);

        for(CustomPage customPage : customPages){
            CustomPageVO customPageVO = new CustomPageVO();
            customPageVO.setCustomPage(customPage);
            customPageVOS.add(customPageVO);
        }
        return R.ok(myPageInfo);
    }

    @PostMapping("/deleteCustomPage")
    public R deleteCustomPage(@RequestBody DeleteCustomPageAO deleteCustomPageAO){
        deleteCustomPageAO.check();
        customPageMapper.deleteById(deleteCustomPageAO.getId());
        return R.ok();
    }

    @PostMapping("/updateCustomPage")
    public R updateCustomPage(@RequestBody UpdateCustomPageAO updateCustomPageAO){
        updateCustomPageAO.check();
        CustomPage customPage = customPageMapper.selectById(updateCustomPageAO.getId());
        if(customPage == null){
            throw new RuntimeException("页面不存在");
        }

        Optional.ofNullable(updateCustomPageAO).map(o-> o.getCustomName())
                .map(o-> o.trim()).filter(o-> !o.equals("")).ifPresent(o-> customPage.setCustomName(o));
        Optional.ofNullable(updateCustomPageAO).map(o-> o.getPageValue())
                .map(o-> o.trim()).filter(o-> !o.equals("")).ifPresent(o-> customPage.setPageValue(o));

        String customName = Optional.ofNullable(updateCustomPageAO).map(o -> o.getCustomName())
                .map(o -> o.trim()).filter(o -> !o.equals("")).orElse(null);
        String pageValue = Optional.ofNullable(updateCustomPageAO).map(o -> o.getPageValue())
                .map(o -> o.trim()).filter(o -> !o.equals("")).orElse(null);

        customPageMapper.update(null, new LambdaUpdateWrapper<CustomPage>()
                .eq(CustomPage::getId, updateCustomPageAO.getId())
                .set(CustomPage::getCustomName, customName)
                .set(CustomPage::getPageValue, pageValue)
        );

        return R.ok();
    }

    @ApiOperation(value = "查询自定义页面详情", notes = "查询自定义页面详情")
    @PostMapping("/customPageInfo")
    public R<CustomPageVO> customPageInfo(@RequestBody CustomPageInfoAO customPageInfoAO) {
        customPageInfoAO.check();

        CustomPage customPage = customPageMapper.selectById(customPageInfoAO.getId());
        if(customPage == null){
            throw new RuntimeException("页面不存在");
        }
        CustomPageVO customPageVO = new CustomPageVO();
        customPageVO.setCustomPage(customPage);
        return R.ok(customPageVO);
    }
}
