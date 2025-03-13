package com.vict.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vict.entity.CustomPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomPageMapper extends BaseMapper<CustomPage> {
    List<CustomPage> selectMyList(@Param("customName")String customName);
}
