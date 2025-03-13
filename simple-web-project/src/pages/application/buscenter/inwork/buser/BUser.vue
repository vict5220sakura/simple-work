<template>
    <div class="m-4">
        <el-button type="primary" @click="addBUser" v-permission="permissionList.addBuser">新增</el-button>
    </div>
    
    <InnerDialog v-model="addDialogVisible" >
        <template #header>
            新增用户
        </template>
        <template #default>
            <div>
                <div class="flex flex-row justify-start items-center m-4">
                    <div class="w-20 required">员工工号: </div>
                    <el-input class="w-36" v-model="buserCode" placeholder="请输入员工工号" />
                </div>
                <div class="flex flex-row justify-start items-center m-4">
                    <div class="w-20 required">用户名: </div>
                    <el-input class="w-36" v-model="username" placeholder="请输入用户名" />
                </div>
                <div class="flex flex-row justify-start items-center m-4">
                    <div class="w-20 required">密码: </div>
                    <el-input class="w-36" v-model="password" placeholder="请输入密码" />
                </div>
                <div class="flex flex-row justify-start items-center m-4">
                    <div class="w-20">角色: </div>
                    <el-select clearable class="w-36" v-model="roleId" placeholder="请选择角色">
                        <el-option v-for="item in roleAll" :key="item.id" :label="item.rolename" :value="item.id"></el-option>
                    </el-select>
                </div>
            </div>
        </template>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="addBUserCancel">取消</el-button>
                <el-button type="primary" @click="addBUserSave">保存</el-button>
            </div>
        </template>
    </InnerDialog>

    <!-- 查询 -->
    <div class="m-4 flex felx-row justify-start items-center" v-permission="permissionList.selectBuser">
        <span class="w-20">
            用户名: 
        </span>
        <el-input clearable v-model="selectUsername" class="w-36 mr-4"></el-input>
        <span class="w-20">
            员工工号: 
        </span>
        <el-input clearable v-model="selectBuserCode" class="w-36 mr-4"></el-input>
    </div>
    <div class="m-4 flex felx-row justify-start items-center" v-permission="permissionList.selectBuser">
        <el-button type="" @click="reset">重置</el-button>
        <el-button type="primary" @click="select">查询</el-button>
    </div>

    <!-- 列表 -->
    <div class="m-4" v-loading="tableLoading" v-permission="permissionList.selectBuser">
        <el-table
            class="h-[calc(100vh-20rem)]"
            :border="true"
            :data="pageInfo?.list"
            style="width: 100%"
            :row-key="(row:any)=>{row.id}"
        >
        <el-table-column prop="buserCode" label="员工工号" width="180"> </el-table-column>
            <el-table-column prop="username" label="用户名" width="180"> </el-table-column>
            <el-table-column prop="rolename" label="角色" width="180"> </el-table-column>
            <el-table-column fixed="right" label="操作" width="180">
                <template #default="scope">
                    <el-button type="primary" @click="updateBuser(scope.row)" 
                        v-permission="permissionList.editBuser" link>编辑</el-button>
                    <el-button type="danger" @click="deleteBuser(scope.row.id)" 
                        v-permission="permissionList.deleteBuser" link>删除</el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>
    <div class="m-4 flex flex-row justify-end" v-permission="permissionList.selectBuser">
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
            编辑用户
        </template>
        <template #default>
            <div>
                <div class="flex flex-row justify-start items-center m-4">
                    <div class="w-20 required">员工工号: </div>
                    <el-input class="w-36" v-model="updateBuserCode" placeholder="请输入员工工号" />
                </div>
                <div class="flex flex-row justify-start items-center m-4">
                    <div class="w-20 required">用户名: </div>
                    <el-input class="w-36" v-model="updateUsername" placeholder="请输入用户名" />
                </div>
                <div class="flex flex-row justify-start items-center m-4">
                    <div class="w-20">密码: </div>
                    <el-input class="w-36" v-model="updatePassword" placeholder="请输入密码" />
                </div>
                <div class="flex flex-row justify-start items-center m-4">
                    <div class="w-20">角色: </div>
                    <el-select clearable class="w-36" v-model="updateRoleId" placeholder="请选择角色">
                        <el-option v-for="item in roleAll" :key="item.id" :label="item.rolename" :value="item.id"></el-option>
                    </el-select>
                </div>
            </div>
        </template>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="updateBUserCancel">取消</el-button>
                <el-button type="primary" @click="updateBUserSave">保存</el-button>
            </div>
        </template>
    </InnerDialog>
</template>

<script setup lang="ts" name="buser">
import InnerDialog from '@/components/utils/InnerDialog.vue'
import BUserHooks from './BUserHooks'

let {
    addBUser,
    permissionList,
    addDialogVisible,
    buserCode,
    username,
    password,
    roleId,
    roleAll,
    addBUserCancel,
    addBUserSave,
    selectUsername,
    selectBuserCode,
    reset,
    select,
    tableLoading,
    pageInfo,
    updateBuser,
    deleteBuser,
    pageRequest,
    updateDialogVisible,
    updateBuserCode,
    updateUsername,
    updatePassword,
    updateRoleId,
    updateBUserCancel,
    updateBUserSave
} = BUserHooks();

</script>

<style scoped></style>
