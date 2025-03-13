package com.vict.controller.customer;

import com.vict.bean.customer.vo.CustomerItemVO;
import com.vict.entity.Customer;
import com.vict.framework.bean.R;
import com.vict.framework.utils.UserContextUtil;
import com.vict.framework.web.ApiPrePath;
import com.vict.mapper.CustomerMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ApiPrePath
@Api
@Slf4j
@RestController
@RequestMapping("/appCustomer")
public class AppCustomerController {
    private static final String SEAGGER_TAGS = "客户(APP)";

    @Autowired
    private CustomerMapper customerMapper;

    @ApiOperation(tags = SEAGGER_TAGS, value = "客户信息", httpMethod = "POST")
    @PostMapping("/customerInfo")
    public R<CustomerItemVO> customerInfo(){
        Long appCustomerId = UserContextUtil.getContext().getAppCustomerId();
        Customer customer = customerMapper.selectById(appCustomerId);
        CustomerItemVO customerItemVO = new CustomerItemVO();
        customerItemVO.setCustomer(customer);
        return R.ok(customerItemVO);
    }
}
