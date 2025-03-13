package com.vict.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vict.entity.Organize;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrganizeMapper extends BaseMapper<Organize> {
    List<Organize> selectAll();
}
