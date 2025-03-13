package com.vict.controller.buser;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.vict.bean.buser.ao.AddUserAO;
import com.vict.bean.buser.ao.DeleteBuserAO;
import com.vict.bean.buser.ao.SelectBuserListAO;
import com.vict.bean.buser.ao.UpdateBuserAO;
import com.vict.bean.buser.vo.BuserVO;
import com.vict.entity.Buser;
import com.vict.entity.Role;
import com.vict.framework.bean.MyPageInfo;
import com.vict.framework.bean.R;
import com.vict.framework.utils.IdUtils;
import com.vict.framework.utils.lock.LockApi;
import com.vict.framework.web.ApiPrePath;
import com.vict.mapper.BuserMapper;
import com.vict.mapper.RoleMapper;
import io.swagger.annotations.Api;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@ApiPrePath
@Api
@Slf4j
@RestController
@RequestMapping("/bUser")
public class BUserController {

    @Autowired
    private BuserMapper buserMapper;

    @Autowired
    private RoleMapper roleMapper;

    /** 添加用户 */
    @PostMapping("/addUser")
    public R addUser(@RequestBody AddUserAO addUserAO){
        LockApi lock = LockApi.getLock("addBuser");
        try{
            lock.lockBlock();
            List<Buser> busers = buserMapper.selectList(new LambdaQueryWrapper<Buser>().eq(Buser::getBuserCode, addUserAO.getBUserCode()));
            if(busers != null && busers.size() > 0){
                throw new RuntimeException("员工工号重复, 请重新输入");
            }

        }finally {
            lock.unlockIfSuccess();
        }

        addUserAO.check();
        Buser bUser = new Buser();
        bUser.setId(IdUtils.snowflakeId());
        bUser.setUsername(addUserAO.getUsername());
        bUser.setPassword(addUserAO.getPassword());
        bUser.setBuserCode(addUserAO.getBUserCode());
        bUser.setRoleId(addUserAO.getRoleId());
        buserMapper.insert(bUser);

        return R.ok();
    }

    @SneakyThrows
    @PostMapping("/selectBuserList")
    public R<MyPageInfo<BuserVO>> selectBuserList(@RequestBody SelectBuserListAO selectBuserListAO){
        // 参数
        String username = Optional.ofNullable(selectBuserListAO).map(o -> o.getUsername()).map(o -> o.trim())
                .filter(o -> !o.equals("")).orElse(null);
        String buserCode = Optional.ofNullable(selectBuserListAO).map(o -> o.getBuserCode()).map(o -> o.trim())
                .filter(o -> !o.equals("")).orElse(null);
        Long organizeId = Optional.ofNullable(selectBuserListAO).map(o -> o.getOrganizeId()).orElse(null);

        // 数据查询
        Page page = PageHelper.startPage(selectBuserListAO.getPageNum(), selectBuserListAO.getPageSize());
        buserMapper.selectMyList(username, buserCode, organizeId);
        List<Buser> busers = Optional.ofNullable(page.getResult()).orElse(new ArrayList<>());

        // role查询
        List<Long> roleIds = busers.stream().map(o -> o.getRoleId()).collect(Collectors.toList());
        List<Role> roles = null;
        if(roleIds != null && roleIds.size() > 0){
            roles = roleMapper.selectBatchIds(roleIds);
        }
        roles = Optional.ofNullable(roles).orElse(new ArrayList<>());

        Map<Long, Role> roleTable = roles.stream().collect(Collectors.toMap(o -> o.getId(), o -> o));


        // 数据封装
        MyPageInfo<BuserVO> myPageInfo = new MyPageInfo<BuserVO>(page);
        List<BuserVO> buserVOS = new ArrayList<BuserVO>(busers.size());
        myPageInfo.setList(buserVOS);
        for(Buser buser : busers){
            BuserVO buserVO = new BuserVO();

            buserVO.setBuser(buser);

            Role role = Optional.ofNullable(buser).map(o -> o.getRoleId()).map(o -> roleTable.get(o)).orElse(null);

            buserVO.setRole(role);

            buserVOS.add(buserVO);
        }
        return R.ok(myPageInfo);
    }

    @PostMapping("/deleteBuser")
    public R deleteBuser(@RequestBody DeleteBuserAO deleteBuserAO){
        deleteBuserAO.check();
        buserMapper.deleteById(deleteBuserAO.getId());
        return R.ok();
    }

    @PostMapping("/updateBuser")
    public R updateBuser(@RequestBody UpdateBuserAO updateBuserAO){
        updateBuserAO.check();
        Buser buser = buserMapper.selectById(updateBuserAO.getId());
        if(buser == null){
            throw new RuntimeException("员工不存在");
        }
        Optional.ofNullable(updateBuserAO).map(o-> o.getUsername()).map(o-> o.trim()).filter(o-> !o.equals(""))
                .ifPresent(o-> buser.setUsername(o));
        Optional.ofNullable(updateBuserAO).map(o-> o.getPassword()).map(o-> o.trim()).filter(o-> !o.equals(""))
                .ifPresent(o-> buser.setPassword(o));
        Optional.ofNullable(updateBuserAO).map(o-> o.getRoleId())
                .ifPresent(o-> buser.setRoleId(o));
        Optional.ofNullable(updateBuserAO).map(o-> o.getBuserCode()).map(o-> o.trim()).filter(o-> !o.equals(""))
                .ifPresent(o-> buser.setBuserCode(o));

        buserMapper.updateById(buser);

        return R.ok();
    }
}
