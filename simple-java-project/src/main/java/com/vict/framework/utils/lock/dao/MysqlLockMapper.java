package com.vict.framework.utils.lock.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vict.framework.utils.lock.entity.MysqlLock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;

@Mapper
public interface MysqlLockMapper extends BaseMapper<MysqlLock> {

    void deleteOverTime(@Param("now")Long now);

    void expire(@Param("lockkey")String lockkey, @Param("overtime") Timestamp overtime);
}
