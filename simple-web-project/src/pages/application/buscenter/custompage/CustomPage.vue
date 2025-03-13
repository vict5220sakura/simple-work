<template>
    <div class="m-4">
        <el-button type="primary" @click="addCustomPage" v-permission="permissionList.addCustomPage">新增</el-button>
    </div>

    <InnerDialog v-model="addCustomPageDialogVisible">
        <template #header>
            新增自定义页面
        </template>
        <template #default>
            <div>
                <div class="flex flex-row justify-start items-center m-4">
                    <div class="w-20 required flex justify-end">页面名称:&nbsp;&nbsp;</div>
                    <el-input class="w-36" v-model="addCustomName" placeholder="请输入页面名称" />
                </div>
                <div class="flex flex-row justify-start items-center m-4">
                    <div class="w-20 flex justify-end">页面数据:&nbsp;&nbsp;</div>
                    <el-input class="w-36" v-model="addPageValue" placeholder="页面数据" />
                    <el-button size="small" type="default" class="ml-4" 
                        @click="addEditorShow">页面编辑</el-button>
                </div>
            </div>
        </template>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="addCustomPageCancel">取消</el-button>
                <el-button type="primary" @click="addCustomPageSave">保存</el-button>
            </div>
        </template>
    </InnerDialog>

    <!-- 查询项 -->
    <div class="m-4 flex felx-row justify-start items-center" v-permission="permissionList.selectCustomPage">
        <span class="w-20">
            页面名称: 
        </span>
        <el-input clearable v-model="selectCustomName" class="w-36 mr-4"></el-input>
    </div>
    <div class="m-4 flex felx-row justify-start items-center" v-permission="permissionList.selectCustomPage">
        <el-button type="" @click="reset">重置</el-button>
        <el-button type="primary" @click="select">查询</el-button>
    </div>

    <!-- 列表 -->
    <div class="m-4" v-loading="tableLoading" v-permission="permissionList.selectCustomPage">
        <el-table
            class="h-[calc(100vh-20rem)]"
            :border="true"
            :data="pageInfo?.list"
            style="width: 100%"
            :row-key="(row:any)=>{row.id}"
        >
            <el-table-column prop="customName" label="页面名称" width="180" show-overflow-tooltip></el-table-column>
            <!-- <el-table-column prop="id" label="id" width="220" show-overflow-tooltip> </el-table-column> -->
            <el-table-column prop="pageValue" label="页面链接" width="180" show-overflow-tooltip>
                <template #default="scope">
                    <el-button type="primary" @click="copyUrl(pageUrl(scope.row.id))" link>点击复制</el-button>
                </template>
            </el-table-column>
            <el-table-column prop="pageValue" label="页面数据" width="180" show-overflow-tooltip> </el-table-column>
            <el-table-column fixed="right" label="操作" width="180">
                <template #default="scope">
                    <el-button type="primary" @click="updateCustomPage(scope.row)" 
                        v-permission="permissionList.editCustomPage" link>编辑</el-button>
                    <el-button type="danger" @click="deleteCustomPage(scope.row.id)" 
                        v-permission="permissionList.deleteCustomPage" link>删除</el-button>
                    <el-button link>
                        <el-link type="primary" :href="pageUrl(scope.row.id)" target="_blank">打开页面</el-link>
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>
    <div class="m-4 flex flex-row justify-end" v-permission="permissionList.selectCustomPage">
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

    <InnerDialog v-model="editDialogVisible" :width="400">
        <template #header>
            编辑自定义页面
        </template>
        <template #default>
            <div>
                <div class="flex flex-row justify-start items-center m-4">
                    <div class="w-20 required flex justify-end">页面名称:&nbsp;&nbsp;</div>
                    <el-input class="w-36" v-model="updateCustomName" placeholder="请输入页面名称" />
                </div>
                <div class="flex flex-row justify-start items-center m-4">
                    <div class="w-20 flex justify-end">页面数据:&nbsp;&nbsp;</div>
                    <el-input class="w-36" v-model="updatePageValue" placeholder="页面数据" />
                    <el-button size="small" type="default" class="ml-4" 
                        @click="updateEditorShow">页面编辑</el-button>
                </div>
            </div>
        </template>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="updateCustomPageCancel">取消</el-button>
                <el-button type="primary" @click="updateCustomPageSave">保存</el-button>
            </div>
        </template>
    </InnerDialog>


    <!-- <el-button @click="dialogVisible = true">富文本编辑器</el-button> -->
    <InnerDialog v-model="editorDialog" :width="1000">
        <WangEditor v-if="editorDialog" v-model="editorValue"></WangEditor>
        <el-button @click="editorCancel">取消</el-button>
        <el-button type="primary" @click="editorSave">保存</el-button>
    </InnerDialog>
</template>

<script setup lang="ts" name="customPage">
import InnerDialog from '@/components/utils/InnerDialog.vue'
import {useCustomPageHooks} from './CustomPageHooks'
import WangEditor from '@/components/utils/WangEditor.vue'

let {
    CustomPageEditorTarget,
    permissionList,
    addCustomPage,
    addCustomPageDialogVisible,
    addCustomName,
    addPageValue,
    addCustomPageCancel,
    addCustomPageSave,
    selectCustomName,
    reset,
    select,
    tableLoading,
    pageRequest,
    pageInfo,
    updateCustomPage,
    editDialogVisible,
    editorDialog,
    editorTarget,
    editorValue,
    editorCancel,
    editorSave,
    updateCustomName,
    updatePageValue,
    updateCustomPageCancel,
    updateCustomPageSave,
    deleteCustomPage,
    pageUrl,
    copyUrl,
    addEditorShow,
    updateEditorShow
} = useCustomPageHooks()

</script>

<style scoped></style>
