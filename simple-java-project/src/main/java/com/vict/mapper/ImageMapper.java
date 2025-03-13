package com.vict.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vict.entity.Image;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ImageMapper extends BaseMapper<Image> {
    List<Image> selectMyList(@Param("attname")String attname, @Param("imageType")Image.ImageType imageType);

    Image selectUp(@Param("orderNum")Integer orderNum);

    Image selectDown(@Param("orderNum")Integer orderNum);

    void setOrderNum(@Param("id")Long id, @Param("orderNum")Integer orderNum);

    void changeImage(@Param("id1")Long id1, @Param("id2")Long id2);

    void moveBatch(@Param("images")List<Image> images);

    List<Image> selectBatchForUpdate(@Param("ids")List<Long> ids);
}
