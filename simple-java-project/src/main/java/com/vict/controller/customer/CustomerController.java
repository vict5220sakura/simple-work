package com.vict.controller.customer;

import cn.hutool.crypto.digest.MD5;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.vict.bean.buser.ao.DeleteBuserAO;
import com.vict.bean.buser.vo.BuserVO;
import com.vict.bean.customer.ao.*;
import com.vict.bean.customer.vo.CustomerItemVO;
import com.vict.entity.Buser;
import com.vict.entity.Customer;
import com.vict.framework.bean.MyPageInfo;
import com.vict.framework.bean.R;
import com.vict.framework.utils.IdUtils;
import com.vict.framework.web.ApiPrePath;
import com.vict.mapper.CustomerMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@ApiPrePath
@Api
@Slf4j
@RestController
@RequestMapping("/customer")
public class CustomerController {

    private static final String SEAGGER_TAGS = "客户";

    @Autowired
    private CustomerMapper customerMapper;

    @ApiOperation(tags = SEAGGER_TAGS, value = "新增客户", httpMethod = "POST")
    @PostMapping("/insertCustomer")
    public R<String> insertCustomer(@RequestBody InsertCustomerAO insertCustomerAO) {
        insertCustomerAO.check();

        Customer customer = new Customer();
        customer.setId(IdUtils.snowflakeId());
        customer.setCustomerPhone(insertCustomerAO.getCustomerPhone());
        customer.setCustomerName(insertCustomerAO.getCustomerName());

        customerMapper.insert(customer);

        return R.ok(customer.getId().toString());
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "查询客户", httpMethod = "POST")
    @PostMapping("/customerList")
    public R<MyPageInfo<CustomerItemVO>> customerList(@RequestBody CustomerListAO customerListAO) {
        String customerName = Optional.ofNullable(customerListAO).map(o -> o.getCustomerName())
                .map(o-> o.trim()).filter(o-> !o.equals("")).orElse(null);
        String customerPhone = Optional.ofNullable(customerListAO).map(o -> o.getCustomerPhone())
                .map(o-> o.trim()).filter(o-> !o.equals("")).orElse(null);

        // 数据查询
        Page page = PageHelper.startPage(customerListAO.getPageNum(), customerListAO.getPageSize());
        customerMapper.selectMyList(customerName, customerPhone);
        List<Customer> customers = Optional.ofNullable(page.getResult()).orElse(new ArrayList<>());

        MyPageInfo<CustomerItemVO> myPageInfo = new MyPageInfo<CustomerItemVO>(page);
        List<CustomerItemVO> customerItemVOS = new ArrayList<CustomerItemVO>(customers.size());
        myPageInfo.setList(customerItemVOS);

        for(Customer customer : customers){
            CustomerItemVO customerItemVO = new CustomerItemVO();
            customerItemVO.setCustomer(customer);
            customerItemVOS.add(customerItemVO);
        }
        return R.ok(myPageInfo);
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "删除客户", httpMethod = "POST")
    @PostMapping("/deleteCustomer")
    public R deleteCustomer(@RequestBody DeleteCustomerAO deleteCustomerAO){
        deleteCustomerAO.check();
        customerMapper.deleteById(deleteCustomerAO.getId());
        return R.ok();
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "更新客户", httpMethod = "POST")
    @PostMapping("/updateCustomer")
    public R updateCustomer(@RequestBody UpdateCustomerAO updateCustomerAO){
        updateCustomerAO.check();
        Customer customer = customerMapper.selectById(updateCustomerAO.getId());
        if(customer == null){
            throw new RuntimeException("客户不存在");
        }

        Optional.ofNullable(updateCustomerAO).map(o-> o.getCustomerName()).map(o-> o.trim()).filter(o-> !o.equals(""))
                .ifPresent(o-> customer.setCustomerName(o));
        Optional.ofNullable(updateCustomerAO).map(o-> o.getCustomerPhone()).map(o-> o.trim()).filter(o-> !o.equals(""))
                .ifPresent(o-> customer.setCustomerPhone(o));

        customerMapper.updateById(customer);

        return R.ok();
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "重置密码", httpMethod = "POST")
    @PostMapping("/resetPassword")
    public R resetPassword(@RequestBody ResetPasswordAO resetPasswordAO){
        resetPasswordAO.check();
        Customer customer = customerMapper.selectById(resetPasswordAO.getId());
        customer.setPassword(MD5.create().digestHex("123456").toUpperCase(Locale.ROOT));
        customerMapper.updateById(customer);
        return R.ok();
    }
}
