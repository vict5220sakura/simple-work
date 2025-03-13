package com.vict.controller.buser;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.vict.entity.Role;
import com.vict.bean.role.ao.AddRoleAO;
import com.vict.bean.role.ao.DeleteRoleAO;
import com.vict.bean.role.ao.SelectRoleListAO;
import com.vict.bean.role.ao.UpdateRoleAO;
import com.vict.framework.bean.MyPageInfo;
import com.vict.framework.bean.R;
import com.vict.framework.utils.IdUtils;
import com.vict.framework.web.ApiPrePath;
import com.vict.mapper.RoleMapper;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@ApiPrePath
@Api
@Slf4j
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleMapper roleMapper;

    /** 添加用户 */
    @PostMapping("/addRole")
    public R addRole(@RequestBody AddRoleAO addRoleAO){

        addRoleAO.check();

        Role role = new Role();
        role.setId(IdUtils.snowflakeId());
        role.setRolename(addRoleAO.getRolename());
        role.setPermissionList(addRoleAO.getPermissionList());

        roleMapper.insert(role);

        return R.ok();
    }

    @PostMapping("/selectRoleList")
    public R<MyPageInfo<Role>> selectList(@RequestBody SelectRoleListAO selectRoleListAO){
        // 参数
        String rolename = Optional.ofNullable(selectRoleListAO).map(o -> o.getRolename()).map(o -> o.trim())
                .filter(o -> !o.equals("")).orElse(null);

        Page<Role> page = PageHelper.startPage(selectRoleListAO.getPageNum(), selectRoleListAO.getPageSize());
        roleMapper.selectMyList(rolename);

        MyPageInfo<Role> myPageInfo = new MyPageInfo<Role>(page);
        return R.ok(myPageInfo);
    }

    @PostMapping("/deleteRole")
    public R deleteRole(@RequestBody DeleteRoleAO deleteRoleAO){
        deleteRoleAO.check();
        roleMapper.deleteById(deleteRoleAO.getId());
        return R.ok();
    }

    @PostMapping("/updateRole")
    public R updateRole(@RequestBody UpdateRoleAO updateRoleAO){
        updateRoleAO.check();
        Role role = roleMapper.selectById(updateRoleAO.getId());

        Optional.ofNullable(updateRoleAO).map(o-> o.getRolename()).map(o-> o.trim()).filter(o-> !o.equals(""))
                .ifPresent(o-> role.setRolename(o));
        Optional.ofNullable(updateRoleAO).map(o-> o.getPermissionList())
                .ifPresent(o-> role.setPermissionList(o));

        roleMapper.updateById(role);

        return R.ok();
    }
}
