<template>
    <div class="m-4">
        <el-button type="primary" @click="addRole" v-permission="vpermissionList.addRole">新增角色</el-button>
    </div>
    
    <InnerDialog v-model="addDialogVisible" >
        <template #header>
            新增角色
        </template>
        <template #default>
            <div>
                <div class="flex flex-row justify-start items-center m-4">
                    <div class="w-20 required">角色名: </div>
                    <el-input class="w-36" v-model="rolename" placeholder="请输入角色名" />
                </div>
                <span class="m-4">权限:</span>
                <div class="flex flex-row justify-start items-center m-4">
                    <PermissionList v-model="permissionList"/>
                </div>
                
            </div>
            
        </template>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="addRoleCancel">取消</el-button>
                <el-button type="primary" @click="addRoleSave">保存</el-button>
            </div>
        </template>
    </InnerDialog>
    
    <!-- 查询 -->
    <div class="m-4 flex felx-row justify-start items-center" v-permission="vpermissionList.selectRole">
        <span class="w-20">
            角色名: 
        </span>
        <el-input clearable v-model="selectRolename" class="w-36 mr-4"></el-input>
    </div>
    <div class="m-4 flex felx-row justify-start items-center" v-permission="vpermissionList.selectRole">
        <el-button type="" @click="reset">重置</el-button>
        <el-button type="primary" @click="select">查询</el-button>
    </div>

    <!-- 列表 -->
    <div class="m-4" v-loading="tableLoading" v-permission="vpermissionList.selectRole">
        <el-table
            class="h-[calc(100vh-20rem)]"
            :border="true"
            :data="pageInfo?.list"
            style="width: 100%"
            :row-key="(row:any)=>{row.id}"
        >
            <el-table-column prop="rolename" label="角色名" width="180"> </el-table-column>
            <el-table-column fixed="right" label="操作" width="180">
                <template #default="scope">
                    <el-button type="primary" @click="updateBuser(scope.row)" 
                        v-permission="vpermissionList.editRole" link>编辑</el-button>
                    <el-button type="danger" @click="deleteRole(scope.row.id)" 
                        v-permission="vpermissionList.deleteRole" link>删除</el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>
    <div class="m-4 flex flex-row justify-end" v-permission="vpermissionList.selectRole">
        <el-pagination 
            background 
            v-model:current-page="pageRequest.pageNum"
            v-model:page-size="pageRequest.pageSize"
            layout="total, sizes, prev, pager, next, jumper" 
            :page-count="Number(pageInfo?.totalPage)"
            :total="Number(pageInfo?.total)"
            @size-change="select"
            @current-change="select"
            prev-text="上一页"
            next-text="下一页"
        >
        </el-pagination>
    </div>


    <InnerDialog v-model="updateDialogVisible" >
        <template #header>
            编辑角色
        </template>
        <template #default>
            <div>
                <div class="flex flex-row justify-start items-center m-4">
                    <div class="w-20">角色名: </div>
                    <el-input class="w-36" v-model="updateRolename" placeholder="请输入角色名" />
                </div>
                <span class="m-4">权限:</span>
                <div class="flex flex-row justify-start items-center m-4">
                    <PermissionList v-model="updatePermissionList"/>
                </div>
                
            </div>
            
        </template>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="updateRoleCancel">取消</el-button>
                <el-button type="primary" @click="updateRoleSave">保存</el-button>
            </div>
        </template>
    </InnerDialog>
</template>

<script setup lang="ts" name="buser">
import InnerDialog from '@/components/utils/InnerDialog.vue'
import PermissionList from '@/components/role/PermissionList.vue'
import {permissionList as vpermissionList}  from '@/config/PermissionConfig'

import {useRoleHooks} from "./RoleHooks"

let {
    addRole,
        addDialogVisible,
        rolename,
        permissionList,
        addRoleCancel,
        addRoleSave,
        selectRolename,
        reset,
        select,
        tableLoading,
        pageInfo,
        updateBuser,
        deleteRole,
        pageRequest,
        updateDialogVisible,
        updateRolename,
        updatePermissionList,
        updateRoleCancel,
        updateRoleSave,
} = useRoleHooks();

</script>

<style scoped></style>
