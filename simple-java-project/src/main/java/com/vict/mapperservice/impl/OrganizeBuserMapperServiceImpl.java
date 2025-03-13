package com.vict.mapperservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vict.entity.OrganizeBuser;
import com.vict.mapper.OrganizeBuserMapper;
import com.vict.mapperservice.OrganizeBuserMapperService;
import org.springframework.stereotype.Service;

@Service
public class OrganizeBuserMapperServiceImpl extends ServiceImpl<OrganizeBuserMapper, OrganizeBuser> implements OrganizeBuserMapperService {
}
