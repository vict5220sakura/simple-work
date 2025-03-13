package com.vict.mapper.chinesepoetry;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vict.entity.chinesepoetry.ChinesePoetryClassify;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ChinesePoetryClassifyMapper extends BaseMapper<ChinesePoetryClassify> {
    ChinesePoetryClassify selectUp(@Param("orderNum")Integer orderNum);

    ChinesePoetryClassify selectDown(@Param("orderNum")Integer orderNum);

    void setOrderNum(@Param("id")Long id,
                     @Param("orderNum")Integer orderNum);
}
