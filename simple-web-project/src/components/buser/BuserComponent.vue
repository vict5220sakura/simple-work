<template>
    <slot name="header"></slot>
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
    <div class="m-4 w-full flex felx-row justify-between items-center" v-permission="permissionList.selectBuser">
        <div class="flex flex-row justify-start items-center">
            <el-button type="" @click="reset">重置</el-button>
            <el-button type="primary" @click="select">查询</el-button>
        </div>
        <el-button type="danger" class="mr-8" @click="removeBuserBatch" v-permission="permissionList.organizeRemoveBuser">批量移除</el-button>
    </div>

    <!-- 列表 -->
    <div class="m-4  " 
        v-loading="tableLoading" 
        v-permission="permissionList.selectBuser">
        <el-table
            class="h-[calc(100vh-30rem)]"
            ref="buserRef"
            :border="true"
            :data="pageInfo?.list"
            style="width: 100%"
            :row-key="(row:any)=>{row.id}"
        >
            <el-table-column type="selection" :selectable="()=>{return true}" width="55" />
            <el-table-column prop="buserCode" label="员工工号" width="180"> </el-table-column>
            <el-table-column prop="username" label="用户名" width="180"> </el-table-column>
            <el-table-column prop="rolename" label="角色" width="180"> </el-table-column>
            <el-table-column fixed="right" label="操作" width="180">
                <template #default="scope">
                    <el-button v-if="showDeleteBtn" v-permission="permissionList.organizeRemoveBuser" type="danger" @click="removeBuser(scope.row.id)" link>移除</el-button>
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
</template>

<script setup lang="ts" name="permissionList">
import { useTemplateRef } from 'vue';
import BuserComponentHooks from './BuserComponentHooks';

// 部门id
let organizeId = defineModel<string>("organizeId")

const emits = defineEmits(["removeBuser", "removeBuserBatch"])

defineProps<{
    showDeleteBtn: boolean
}>()

let {
    init,
    permissionList,
    selectUsername,
    selectBuserCode,
    reset,
    select,
    tableLoading,
    pageInfo,
    pageRequest,
    clearSelection,
    removeBuser,
    getSelectionRows,
    removeBuserBatch
} = BuserComponentHooks();

defineExpose({clearSelection, select, getSelectionRows})

init(organizeId, emits)

</script>

<style scoped>

</style>
