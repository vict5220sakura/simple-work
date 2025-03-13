package com.vict.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vict.entity.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {
    List<Customer> selectMyList(@Param("customerName")String customerName,
                                @Param("customerPhone")String customerPhone);
}
