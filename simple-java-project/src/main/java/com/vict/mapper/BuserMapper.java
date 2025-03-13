package com.vict.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vict.entity.Buser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BuserMapper extends BaseMapper<Buser> {
    List<Buser> selectMyList(@Param("username")String username,
                             @Param("buserCode")String buserCode,
                             @Param("organizeId")Long organizeId
    );
}
