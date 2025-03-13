package com.vict.mapperservice.chinesepoetry.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vict.entity.chinesepoetry.ChinesePoetry;
import com.vict.mapper.chinesepoetry.ChinesePoetryMapper;
import com.vict.mapperservice.chinesepoetry.ChinesePoetryMapperService;
import org.springframework.stereotype.Service;

@Service
public class ChinesePoetryMapperServiceImpl extends ServiceImpl<ChinesePoetryMapper, ChinesePoetry> implements ChinesePoetryMapperService {
}
