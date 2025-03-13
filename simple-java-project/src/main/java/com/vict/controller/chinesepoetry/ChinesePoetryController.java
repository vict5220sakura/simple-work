package com.vict.controller.chinesepoetry;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.vict.bean.buser.ao.UpdateBuserAO;
import com.vict.bean.chinesepoetry.ao.*;
import com.vict.bean.chinesepoetry.vo.ChinesePoetryVO;
import com.vict.bean.customer.ao.DeleteCustomerAO;
import com.vict.bean.customer.vo.CustomerItemVO;
import com.vict.entity.Buser;
import com.vict.entity.Customer;
import com.vict.entity.chinesepoetry.ChinesePoetry;
import com.vict.framework.bean.FileInfo;
import com.vict.framework.bean.MyPageInfo;
import com.vict.framework.bean.R;
import com.vict.framework.utils.IdUtils;
import com.vict.framework.web.ApiPrePath;
import com.vict.mapper.chinesepoetry.ChinesePoetryMapper;
import com.vict.mapperservice.chinesepoetry.ChinesePoetryMapperService;
import com.vict.utils.FileUtils;
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
@RequestMapping("/chinesePoetry")
public class ChinesePoetryController {

    private static final String SEAGGER_TAGS = "古诗";

    @Autowired
    private ChinesePoetryMapper chinesePoetryMapper;

    @Autowired
    private ChinesePoetryMapperService chinesePoetryMapperService;

    @ApiOperation(tags = SEAGGER_TAGS, value = "新增古诗", httpMethod = "POST")
    @PostMapping("/insertChinesePoetry")
    public R<String> insertChinesePoetry(@RequestBody InsertChinesePoetryAO insertChinesePoetryAO) {
        insertChinesePoetryAO.check();

        String title = Optional.ofNullable(insertChinesePoetryAO).map(o -> o.getTitle())
                .map(o -> o.trim()).filter(o-> !o.equals("")).orElse(null);
        String author = Optional.ofNullable(insertChinesePoetryAO).map(o -> o.getAuthor())
                .map(o -> o.trim()).filter(o-> !o.equals("")).orElse(null);
        Long classifyId = Optional.ofNullable(insertChinesePoetryAO).map(o -> o.getClassifyId()).orElse(null);
        String paragraphs = Optional.ofNullable(insertChinesePoetryAO).map(o -> o.getParagraphs())
                .map(o -> o.trim()).filter(o-> !o.equals("")).orElse(null);

        ChinesePoetry chinesePoetry = new ChinesePoetry();
        chinesePoetry.setId(IdUtils.snowflakeId());
        chinesePoetry.setTitle(title);
        chinesePoetry.setAuthor(author);
        chinesePoetry.setClassifyId(classifyId);
        chinesePoetry.setParagraphs(paragraphs);

        chinesePoetryMapper.insert(chinesePoetry);
        return R.ok();
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "查询古诗", httpMethod = "POST")
    @PostMapping("/selectChinesePoetry")
    public R<MyPageInfo<ChinesePoetryVO>> selectChinesePoetry(@RequestBody SelectChinesePoetryAO selectChinesePoetryAO) {
        String title = Optional.ofNullable(selectChinesePoetryAO).map(o -> o.getTitle())
                .map(o-> o.trim()).filter(o-> !o.equals("")).orElse(null);
        String author = Optional.ofNullable(selectChinesePoetryAO).map(o -> o.getAuthor())
                .map(o-> o.trim()).filter(o-> !o.equals("")).orElse(null);
        Long classifyId = Optional.ofNullable(selectChinesePoetryAO).map(o -> o.getClassifyId()).orElse(null);

        // 数据查询
        Page page = PageHelper.startPage(selectChinesePoetryAO.getPageNum(), selectChinesePoetryAO.getPageSize());
        chinesePoetryMapper.selectMyList(title, author, classifyId);
        List<ChinesePoetry> chinesePoetrys = Optional.ofNullable(page.getResult()).orElse(new ArrayList<>());

        MyPageInfo<ChinesePoetryVO> myPageInfo = new MyPageInfo<ChinesePoetryVO>(page);
        List<ChinesePoetryVO> ChinesePoetryVOS = new ArrayList<ChinesePoetryVO>(chinesePoetrys.size());
        myPageInfo.setList(ChinesePoetryVOS);

        for(ChinesePoetry chinesePoetry : chinesePoetrys){
            ChinesePoetryVO chinesePoetryVO = new ChinesePoetryVO();
            chinesePoetryVO.setChinesePoetry(chinesePoetry);
            ChinesePoetryVOS.add(chinesePoetryVO);
        }
        return R.ok(myPageInfo);
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "删除古诗", httpMethod = "POST")
    @PostMapping("/deleteChinesePoetry")
    public R deleteChinesePoetry(@RequestBody DeleteChinesePoetryAO deleteChinesePoetryAO){
        deleteChinesePoetryAO.check();
        chinesePoetryMapper.deleteById(deleteChinesePoetryAO.getId());
        return R.ok();
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "批量删除古诗", httpMethod = "POST")
    @PostMapping("/deleteChinesePoetryMany")
    public R deleteChinesePoetry(@RequestBody DeleteChinesePoetryManyAO deleteChinesePoetryManyAO){
        List<Long> chinesePoetryIds = deleteChinesePoetryManyAO.getChinesePoetryIds();
        chinesePoetryMapper.deleteBatchIds(chinesePoetryIds);

        return R.ok();
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "修改古诗", httpMethod = "POST")
    @PostMapping("/updateChinesePoetry")
    public R updateChinesePoetry(@RequestBody UpdateChinesePoetryAO updateChinesePoetryAO){
        updateChinesePoetryAO.check();
        ChinesePoetry chinesePoetry = chinesePoetryMapper.selectById(updateChinesePoetryAO.getId());
        if(chinesePoetry == null){
            throw new RuntimeException("古诗不存在");
        }
        Optional.ofNullable(updateChinesePoetryAO).map(o-> o.getTitle()).map(o-> o.trim()).filter(o-> !o.equals(""))
                .ifPresent(o-> chinesePoetry.setTitle(o));
        Optional.ofNullable(updateChinesePoetryAO).map(o-> o.getAuthor()).map(o-> o.trim()).filter(o-> !o.equals(""))
                .ifPresent(o-> chinesePoetry.setAuthor(o));
        Optional.ofNullable(updateChinesePoetryAO).map(o-> o.getParagraphs())
                .ifPresent(o-> chinesePoetry.setParagraphs(o));


        chinesePoetryMapper.updateById(chinesePoetry);

        return R.ok();
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "古诗修改分类", httpMethod = "POST")
    @PostMapping("/changeChinesePoetryClassify")
    public R changeChinesePoetryClassify(@RequestBody ChangeChinesePoetryClassifyAO changeChinesePoetryClassifyAO){
        changeChinesePoetryClassifyAO.check();

        chinesePoetryMapper.changeChinesePoetryClassify(
                changeChinesePoetryClassifyAO.getChinesePoetryIds(),
                changeChinesePoetryClassifyAO.getChinesePoetryClassifyId()
        );
        return R.ok();
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "导入数据", httpMethod = "POST")
    @PostMapping("/importData")
    public R importData(@RequestBody ImportDataAO importDataAO){
        importDataAO.check();

        if(importDataAO.getFileUrl() != null && !importDataAO.getFileUrl().trim().equals("")){
            FileInfo fileInfo = FileUtils.downloadFile(importDataAO.getFileUrl());
            String filestr = FileUtil.readString(fileInfo.getFile().getAbsolutePath(), "utf-8");
            importDataByStr(filestr, importDataAO.getClassifyId());
        }

        if(importDataAO.getImportData() != null && !importDataAO.getImportData().trim().equals("")){
            importDataByStr(importDataAO.getImportData(), importDataAO.getClassifyId());
        }

        return R.ok();
    }

    public void importDataByStr(String dataStr, Long classifyId){
        JSONArray arr = JSONArray.parseArray(dataStr);

        List<ChinesePoetry> list = new ArrayList<>();

        for(int i = 0 ; i < arr.size() ;i++){
            JSONObject json = arr.getJSONObject(i);
            String title = json.getString("title");

            if(title == null){
                title = json.getString("chapter");
            }

            if(title == null){
                title = json.getString("rhythmic");
            }

            String author = json.getString("author");

            JSONArray paragraphs = json.getJSONArray("paragraphs");
            if(paragraphs == null){
                try{
                    paragraphs = json.getJSONArray("content");
                }catch(Exception e){
                    log.error("", e);
                }
            }
            if(paragraphs == null){
                paragraphs = json.getJSONArray("para");
            }

            String cdesc = null;

            JSONArray comment = json.getJSONArray("comment");
            if(comment != null){
                cdesc = json.getString("content");
                paragraphs = comment;
            }

            String paragraph = null;
            if(paragraphs != null){
                paragraph = "";
                for(int x = 0 ; x < paragraphs.size() ; x++){
                    String paragraphItem = paragraphs.getString(x);
                    paragraph += paragraphItem;
                    if(x != paragraphs.size() - 1){
                        paragraph += "\n";
                    }
                }
            }

            ChinesePoetry chinesePoetry = new ChinesePoetry();
            chinesePoetry.setId(IdUtils.snowflakeId());
            chinesePoetry.setTitle(title);
            chinesePoetry.setAuthor(author);
            chinesePoetry.setClassifyId(classifyId);
            chinesePoetry.setParagraphs(paragraph);
            chinesePoetry.setCdesc(cdesc);

            list.add(chinesePoetry);
        }

        chinesePoetryMapperService.saveBatch(list);
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "古诗移动", httpMethod = "POST")
    @PostMapping("/move")
    public R move(@RequestBody MoveAO moveAO){
        moveAO.check();

        Long id = moveAO.getId();

        ChinesePoetry chinesePoetry = chinesePoetryMapper.selectById(id);

        if (moveAO.getDirection().equals("up")){

            ChinesePoetry chinesePoetryTemp = chinesePoetryMapper.selectUp(chinesePoetry.getOrderNum(), chinesePoetry.getClassifyId());

            if(chinesePoetryTemp == null){
                throw new RuntimeException("没有上一条");
            }

            chinesePoetryMapper.setOrderNum(chinesePoetry.getId(), chinesePoetryTemp.getOrderNum());
            chinesePoetryMapper.setOrderNum(chinesePoetryTemp.getId(), chinesePoetry.getOrderNum());

        }else if(moveAO.getDirection().equals("down")){

            ChinesePoetry chinesePoetryTemp = chinesePoetryMapper.selectDown(chinesePoetry.getOrderNum(), chinesePoetry.getClassifyId());

            if(chinesePoetryTemp == null){
                throw new RuntimeException("没有下一条");
            }

            chinesePoetryMapper.setOrderNum(chinesePoetry.getId(), chinesePoetryTemp.getOrderNum());
            chinesePoetryMapper.setOrderNum(chinesePoetryTemp.getId(), chinesePoetry.getOrderNum());
        }

        return R.ok();
    }
}
