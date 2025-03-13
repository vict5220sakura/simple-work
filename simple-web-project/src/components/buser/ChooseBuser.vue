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
    <div class="m-4 flex felx-row justify-start items-center" v-permission="permissionList.selectBuser">
        <el-button type="" @click="reset">重置</el-button>
        <el-button type="primary" @click="select">查询</el-button>
    </div>

    <!-- 列表 -->
    <div class="m-4" 
        v-loading="tableLoading" 
        v-permission="permissionList.selectBuser">
        <el-table
            class="h-[30rem]"
            ref="buserRef"
            :border="true"
            :data="pageInfo?.list"
            style="width: 100%"
            :row-key="(row:any)=>{row.id}"
        >
            <el-table-column type="selection" :selectable="selectable" width="55" />
            <el-table-column prop="buserCode" label="员工工号" width="180"> </el-table-column>
            <el-table-column prop="username" label="用户名" width="180"> </el-table-column>
            <el-table-column prop="rolename" label="角色" width="180"> </el-table-column>
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
import ChooseBuserHooks from './ChooseBuserHooks';
import type { CheckFunc } from './ChooseBuserTypes';
// import {defineModel} from "vue"


// 部门id
let organizeId = defineModel<string>("organizeId")
const props = defineProps<{
    checkChoose?:CheckFunc
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
    selectable,
    clearSelection,
    getSelectionRows
} = ChooseBuserHooks();

defineExpose({clearSelection, getSelectionRows})

init(organizeId, props)

</script>

<style scoped>

</style>
