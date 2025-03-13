package com.vict.mapperservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vict.entity.Testtable;
import com.vict.mapper.TesttableMapper;
import com.vict.mapperservice.TesttableMapperService;
import org.springframework.stereotype.Service;

@Service
public class TesttableMapperServiceImpl extends ServiceImpl<TesttableMapper, Testtable> implements TesttableMapperService {
}
