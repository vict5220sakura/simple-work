package com.vict.mapper.chinesepoetry;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vict.entity.chinesepoetry.ChinesePoetry;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChinesePoetryMapper extends BaseMapper<ChinesePoetry> {

    List<ChinesePoetry> selectMyList(@Param("title")String title,
                                     @Param("author")String author,
                                     @Param("classifyId")Long classifyId);

    void moveClassify(@Param("idDel")Long idDel, @Param("idTo")Long idTo);

    void changeChinesePoetryClassify(@Param("chinesePoetryIds")List<Long> chinesePoetryIds,
                                     @Param("chinesePoetryClassifyId")Long chinesePoetryClassifyId);

    ChinesePoetry selectUp(@Param("orderNum")Integer orderNum,
                           @Param("classifyId")Long classifyId);

    ChinesePoetry selectDown(@Param("orderNum")Integer orderNum,
                             @Param("classifyId")Long classifyId);

    void setOrderNum(@Param("id")Long id,
                     @Param("orderNum")Integer orderNum);
}
