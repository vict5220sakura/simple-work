<template>
    <div class="m-4">
        <el-button type="primary" @click="addCustomer" v-permission="permissionList.addCustomer">新增</el-button>
    </div>
    <InnerDialog v-model="addDialogVisible" :width="400">
        <template #header>
            新增客户
        </template>
        <template #default>
            <div class="m-4">
                <div>
                    <div class="flex flex-row justify-start items-center m-4">
                        <div class="w-20 required">手机号: </div>
                        <el-input class="w-48" v-model="addCustomerPhone" placeholder="请输入手机号" />
                    </div>
                    <div class="flex flex-row justify-start items-center m-4">
                        <div class="w-20 required">姓名: </div>
                        <el-input class="w-48" v-model="addCustomerName" placeholder="请输入姓名" />
                    </div>
                </div>
            </div>
        </template>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="addKeyValueCancel">取消</el-button>
                <el-button type="primary" @click="addKeyValueSave">保存</el-button>
            </div>
        </template>
    </InnerDialog>

    <!-- 查询项 -->
    <div class="m-4 flex felx-row justify-start items-center" v-permission="permissionList.selectCustomer">
        <span class="w-20">
            手机号: 
        </span>
        <el-input clearable v-model="selectCustomerPhone" class="w-36 mr-4"></el-input>
        <span class="w-20">
            姓名: 
        </span>
        <el-input clearable v-model="selectCustomerName" class="w-36 mr-4"></el-input>
    </div>
    <div class="m-4 flex felx-row justify-start items-center" v-permission="permissionList.selectCustomer">
        <el-button type="" @click="reset">重置</el-button>
        <el-button type="primary" @click="select">查询</el-button>
    </div>

    <!-- 列表 -->
    <div class="m-4" v-loading="tableLoading" v-permission="permissionList.selectCustomer">
        <el-table
            class="h-[calc(100vh-20rem)]"
            :border="true"
            :data="pageInfo?.list"
            style="width: 100%"
            :row-key="(row:any)=>{row.id}"
        >
            <el-table-column prop="customerName" label="姓名" width="180" show-overflow-tooltip> </el-table-column>
            <el-table-column prop="customerPhone" label="手机号" width="180" show-overflow-tooltip> </el-table-column>
            <el-table-column fixed="right" label="操作" width="180">
                <template #default="scope">
                    <el-button type="primary" @click="updateCustomer(scope.row)" 
                        v-permission="permissionList.editCustomer" link>编辑</el-button>
                    <el-button type="danger" @click="deleteCustomer(scope.row.id)" 
                        v-permission="permissionList.deleteCustomer" link>删除</el-button>
                    <el-button type="warning" @click="resetPassword(scope.row.id)" 
                        v-permission="permissionList.resetCustomerPassword" link>重置密码</el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>
    <div class="m-4 flex flex-row justify-end" v-permission="permissionList.selectCustomer">
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

    <InnerDialog v-model="updateDialogVisible" :width="400">
        <template #header>
            编辑客户
        </template>
        <template #default>
            <div class="m-4">
                <div>
                    <div class="flex flex-row justify-start items-center m-4">
                        <div class="w-20 required">手机号: </div>
                        <el-input class="w-48" v-model="updateCustomerPhone" placeholder="请输入手机号" />
                    </div>
                    <div class="flex flex-row justify-start items-center m-4">
                        <div class="w-20 required">姓名: </div>
                        <el-input class="w-48" v-model="updateCustomerName" placeholder="请输入姓名" />
                    </div>
                </div>
            </div>
        </template>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="updateCustomerCancel">取消</el-button>
                <el-button type="primary" @click="updateCustomerSave">保存</el-button>
            </div>
        </template>
    </InnerDialog>
</template>

<script setup lang="ts" name="customerOption">
import {useCustomerOptionHooks} from "./CustomerOptionHooks"
import {permissionList} from '@/config/PermissionConfig'
import InnerDialog from '@/components/utils/InnerDialog.vue'

let {
    addCustomer,
    addDialogVisible,
    addCustomerPhone,
    addCustomerName,
    addKeyValueCancel,
    addKeyValueSave,
    selectCustomerPhone,
    selectCustomerName,
    reset,
    select,
    tableLoading,
    pageInfo,
    pageRequest,
    deleteCustomer,
    resetPassword,
    updateCustomer,
    updateCustomerPhone,
    updateCustomerName,
    updateDialogVisible,
    updateCustomerCancel,
    updateCustomerSave
} = useCustomerOptionHooks()
</script>

<style scoped></style>
@/config/PermissionConfig