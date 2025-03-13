package com.vict.controller.customerlogin;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.vict.bean.app.AppConfig;
import com.vict.bean.applogin.ao.AppLoginInAO;
import com.vict.bean.applogin.dto.AppToken;
import com.vict.bean.applogin.vo.AppLoginInVO;
import com.vict.bean.applogin.vo.IsAppLoginVO;
import com.vict.bean.buserlogin.vo.IsLoginVO;
import com.vict.config.Common;
import com.vict.entity.Buser;
import com.vict.entity.Customer;
import com.vict.framework.bean.R;
import com.vict.framework.keyvalue.entity.KeyValue;
import com.vict.framework.utils.IdUtils;
import com.vict.framework.utils.UserContextUtil;
import com.vict.framework.utils.cache.CacheUtils;
import com.vict.framework.web.ApiPrePath;
import com.vict.mapper.CustomerMapper;
import com.vict.utils.KeyValueUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;
import java.util.Optional;

@ApiPrePath
@Api
@Slf4j
@RestController
@RequestMapping("/customerLogin")
public class CustomerLoginController {
    private static final String SEAGGER_TAGS = "客户登录";

    @Autowired
    private CustomerMapper customerMapper;

    @ApiOperation(tags = SEAGGER_TAGS, value = "app登录", httpMethod = "POST")
    @PostMapping("/appLoginIn")
    public R<AppLoginInVO> loginIn(@RequestBody AppLoginInAO appLoginInAO) {
        appLoginInAO.check();
        Customer customer = customerMapper.selectOne(new LambdaQueryWrapper<Customer>()
                .eq(Customer::getCustomerPhone, appLoginInAO.getPhone())
                .eq(Customer::getPassword, appLoginInAO.getPassword().toUpperCase(Locale.ROOT))
        );
        if(customer == null){
            throw new RuntimeException("手机号或密码错误");
        }

        String aToken = IdUtils.getSalt(32, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray());

        AppToken appToken = new AppToken();
        appToken.setAToken(aToken);
        appToken.setCustomer(customer);

        CacheUtils.addCache("appToken_" + aToken, JSONObject.toJSONString(appToken), Common.appLoginMillSeconds);

        AppLoginInVO appLoginInVO = new AppLoginInVO();
        appLoginInVO.setAToken(aToken);
        return R.ok(appLoginInVO);
    }

    @ApiOperation(tags = SEAGGER_TAGS, value = "app配置", httpMethod = "POST")
    @PostMapping("/getAppConfig")
    public R<AppConfig> getAppConfig(){
        AppConfig appConfig = new AppConfig();

        KeyValue appDebugModeKeyValue = KeyValueUtil.getValueByKey("app_debugMode");
        appConfig.setDebugMode(appDebugModeKeyValue.getValue1());

        KeyValue appLogSwitchKeyValue = KeyValueUtil.getValueByKey("app_logSwitch");
        appConfig.setLogSwitch(appLogSwitchKeyValue.getValue1());

        return R.ok(appConfig);
    }

    @ApiOperation(value = "登录测试", notes = "登录测试")
    @PostMapping("/isAppLogin")
    public R<IsAppLoginVO> isAppLogin(){
        IsAppLoginVO isAppLoginVO = new IsAppLoginVO();
        if(UserContextUtil.isAppUserLogin()){
            isAppLoginVO.setIsLogin(IsAppLoginVO.IsLogin.loginIn);
        }else{
            isAppLoginVO.setIsLogin(IsAppLoginVO.IsLogin.loginOut);
        }
        return R.ok(isAppLoginVO);
    }
}
