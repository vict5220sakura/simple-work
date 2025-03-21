package com.vict.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vict.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    List<Role> selectMyList(@Param("rolename")String rolename);
}
