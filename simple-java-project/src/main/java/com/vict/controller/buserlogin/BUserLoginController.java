package com.vict.controller.buserlogin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.vict.bean.buser.vo.BuserVO;
import com.vict.bean.buserlogin.vo.IsLoginVO;
import com.vict.entity.Buser;
import com.vict.entity.Role;
import com.vict.bean.buserlogin.ao.LoginAO;
import com.vict.bean.buserlogin.dto.BUserToken;
import com.vict.bean.buserlogin.vo.LoginVO;
import com.vict.bean.buserlogin.vo.SelectPermissionVO;
import com.vict.config.Common;
import com.vict.framework.bean.R;
import com.vict.framework.utils.IdUtils;
import com.vict.framework.utils.UserContextUtil;
import com.vict.framework.utils.cache.CacheUtils;
import com.vict.framework.web.ApiPrePath;
import com.vict.mapper.BuserMapper;
import com.vict.mapper.RoleMapper;
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
@RequestMapping("/bUserLogin")
public class BUserLoginController {

    @Autowired
    private BuserMapper bUserMapper;

    @Autowired
    private RoleMapper roleMapper;

    /**
     * 登录, 要么成功 要么失败, 要么报错
     * @param loginAO
     * @return
     */
    @ApiOperation(value = "登录", notes = "登录")
    @PostMapping("/loginIn")
    public R<LoginVO> loginIn(@RequestBody LoginAO loginAO) {
        loginAO.check();

        Buser bUser = bUserMapper.selectOne(new LambdaQueryWrapper<Buser>()
                .eq(Buser::getBuserCode, loginAO.getBuserCode())
                .eq(Buser::getPassword, loginAO.getPassword().toLowerCase(Locale.ROOT))
        );
        if(bUser == null){
            throw new RuntimeException("账号或密码错误");
        }

        String token = IdUtils.getSalt(32, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray());

        BUserToken bUserToken = new BUserToken();
        bUserToken.setToken(token);
        bUserToken.setBUserId(bUser.getId());
        bUserToken.setUsername(bUser.getUsername());
        bUserToken.setPassword(bUser.getPassword());
        bUserToken.setBUserCode(bUser.getBuserCode());

        CacheUtils.addCache("token_" + token, JSONObject.toJSONString(bUserToken), Common.loginMillSeconds);

        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);

        // 权限封装
        Role role = Optional.ofNullable(bUser.getRoleId()).map(o -> roleMapper.selectById(o)).orElse(null);
        loginVO.setPermissionList(Optional.ofNullable(role).map(o -> o.getPermissionList()).orElse(null));
        loginVO.setBuserCode(bUser.getBuserCode());
        log.info("登录, loginVO={}, role={}", JSONObject.toJSONString(loginVO), role);
        return R.ok(loginVO);
    }

    @ApiOperation(value = "登录测试", notes = "登录测试")
    @PostMapping("/loginTest")
    public R loginTest(){
        return R.ok();
    }

    @ApiOperation(value = "登出", notes = "登出")
    @PostMapping("/loginOut")
    public R loginOut(){
        String token = Optional.ofNullable(UserContextUtil.getContext()).map(o -> o.getToken())
                .orElse(null);
        if(token != null){
            CacheUtils.deleteCache("token_" + token);
        }

        return R.ok();
    }

    @ApiOperation(value = "登录测试", notes = "登录测试")
    @PostMapping("/buserInfo")
    public R<BuserVO> buserInfo(){
        Long buserId = Optional.ofNullable(UserContextUtil.getContext())
                .map(o-> o.getBuserId()).orElse(null);
        if(buserId == null){
            return R.ok();
        }

        Buser buser = bUserMapper.selectById(buserId);
        Role role = Optional.ofNullable(buser).map(o -> o.getRoleId())
                .map(o -> roleMapper.selectById(o))
                .orElse(null);


        BuserVO buserVO = new BuserVO();
        buserVO.setBuser(buser);
        buserVO.setRole(role);

        return R.ok(buserVO);
    }

    @ApiOperation(value = "登录测试", notes = "登录测试")
    @PostMapping("/isLogin")
    public R<IsLoginVO> isLogin(){
        IsLoginVO isLoginVO = new IsLoginVO();
        if(UserContextUtil.isBUserLogin()){
            isLoginVO.setIsLogin(IsLoginVO.IsLogin.loginIn);
        }else{
            isLoginVO.setIsLogin(IsLoginVO.IsLogin.loginOut);
        }
        return R.ok(isLoginVO);
    }

    @ApiOperation(value = "刷新权限", notes = "刷新权限")
    @PostMapping("/selectPermission")
    public R<SelectPermissionVO> selectPermission(){
        Long buserId = UserContextUtil.getContext().getBuserId();
        Buser buser = bUserMapper.selectById(buserId);

        JSONArray permissionList = Optional.ofNullable(buser).map(o -> o.getRoleId()).map(o -> roleMapper.selectById(o))
                .map(o -> o.getPermissionList()).orElse(null);

        SelectPermissionVO selectPermissionVO = new SelectPermissionVO();
        selectPermissionVO.setPermissionList(permissionList);

        return R.ok(selectPermissionVO);
    }
}
