package com.vict.controller.image;

import cn.hutool.core.util.URLUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.vict.bean.image.ao.*;
import com.vict.bean.image.vo.ImageVO;
import com.vict.entity.Image;
import com.vict.framework.bean.MyPageInfo;
import com.vict.framework.bean.R;
import com.vict.framework.utils.IdUtils;
import com.vict.framework.utils.QiNiuUtils;
import com.vict.framework.web.ApiPrePath;
import com.vict.mapper.ImageMapper;
import com.vict.service.impl.image.ImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@ApiPrePath
@Api
@Slf4j
@RestController
@RequestMapping("/image")
public class ImageController {

    private static final String SEAGGER_TAGS = "图床";

    @Autowired
    private ImageMapper imageMapper;

    @Autowired
    private ImageService imageService;


    @ApiOperation(tags = SEAGGER_TAGS, value = "新增图片", httpMethod = "POST")
    @PostMapping("/insertImage")
    public R<String> insertImage(@RequestBody InsertImageAO insertImageAO) {
        insertImageAO.check();

        Image image = new Image();
        image.setId(IdUtils.snowflakeId());
        image.setUrl(insertImageAO.getUrl());
        image.setSvgCode(insertImageAO.getSvgCode());
        image.setBase64Code(insertImageAO.getBase64Code());

        image.setAttname(URLUtil.decode(QiNiuUtils.getAttname(insertImageAO.getUrl())));

        image.setImageType(insertImageAO.getImageType());

        imageMapper.insert(image);

        return R.ok();
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "列表", httpMethod = "POST")
    @PostMapping("/select")
    public R<MyPageInfo<ImageVO>> selectImage(@RequestBody SelectImageAO selectImageAO){
        String attname = Optional.ofNullable(selectImageAO).map(o -> o.getAttname()).map(o -> o.trim())
                .filter(o -> !o.equals("")).orElse(null);
        Image.ImageType imageType = Optional.ofNullable(selectImageAO).map(o -> o.getImageType()).orElse(null);

        // 数据查询
        Page page = PageHelper.startPage(selectImageAO.getPageNum(), selectImageAO.getPageSize());

        imageMapper.selectMyList(attname, imageType);

        List<Image> pages = Optional.ofNullable(page.getResult()).orElse(new ArrayList<>());

        MyPageInfo<ImageVO> myPageInfo = new MyPageInfo<ImageVO>(page);
        List<ImageVO> pageVOS = new ArrayList<ImageVO>(pages.size());
        myPageInfo.setList(pageVOS);

        for(Image image : pages){
            ImageVO vo = new ImageVO();
            vo.setImage(image);
            pageVOS.add(vo);
        }
        return R.ok(myPageInfo);
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "删除图片", httpMethod = "POST")
    @PostMapping("/deleteImage")
    public R<String> deleteImage(@RequestBody DeleteImageAO deleteImageAO) {
        deleteImageAO.check();

        imageMapper.deleteById(deleteImageAO.getId());

        return R.ok();
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "修改图片名称", httpMethod = "POST")
    @PostMapping("/updateImageAttname")
    public R<String> updateImageAttname(@RequestBody UpdateImageAttnameAO updateImageAttnameAO) {
        updateImageAttnameAO.check();

        Image image = imageMapper.selectById(updateImageAttnameAO.getId());
        image.setAttname(updateImageAttnameAO.getAttname());

        imageMapper.updateById(image);

        return R.ok();
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "图片移动", httpMethod = "POST")
    @PostMapping("/moveImage")
    public R moveImage(@RequestBody MoveImageAO moveImageAO){
        moveImageAO.check();

        Long id = moveImageAO.getId();

        Image image = imageMapper.selectById(id);

        if (moveImageAO.getDirection().equals("up")){

            Image imageTemp = imageMapper.selectUp(image.getOrderNum());

            if(imageTemp == null){
                throw new RuntimeException("没有上一条");
            }

            imageMapper.setOrderNum(image.getId(), imageTemp.getOrderNum());
            imageMapper.setOrderNum(imageTemp.getId(), image.getOrderNum());

        }else if(moveImageAO.getDirection().equals("down")){

            Image imageTemp = imageMapper.selectDown(image.getOrderNum());

            if(imageTemp == null){
                throw new RuntimeException("没有下一条");
            }

            imageMapper.setOrderNum(image.getId(), imageTemp.getOrderNum());
            imageMapper.setOrderNum(imageTemp.getId(), image.getOrderNum());
        }

        return R.ok();
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "批量删除", httpMethod = "POST")
    @PostMapping("/deleteImageMany")
    public R deleteImageMany(@RequestBody DeleteImageManyAO deleteImageManyAO){
        deleteImageManyAO.check();
        imageMapper.deleteBatchIds(deleteImageManyAO.getIds());
        return R.ok();
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "图片交换", httpMethod = "POST")
    @PostMapping("/changeImage")
    public R changeImage(@RequestBody ChangeImageAO changeImageAO){
        changeImageAO.check();

        imageMapper.changeImage(changeImageAO.getId1(), changeImageAO.getId2());

        return R.ok();
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "图片批量移动", httpMethod = "POST")
    @PostMapping("/batchMove")
    public R changeImage(@RequestBody BatchMoveAO batchMoveAO){
        batchMoveAO.check();

        imageService.changeImage(batchMoveAO.getIds());

        return R.ok();
    }
}
