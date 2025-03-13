package com.vict.service;

import com.vict.framework.settimeout.dto.SetTimeoutDto;

public interface TestService {

    void test();

    void insert();
    void testSimpleTask();

    void testSetTimeout(SetTimeoutDto setTimeoutDto);
}
