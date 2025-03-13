<template>
    <div class="m-4">
        <el-button type="primary" @click="addKeyValue" v-permission="permissionList.addKeyValue">新增</el-button>
    </div>
    <InnerDialog v-model="addDialogVisible" :width="800">
        <template #header>
            新增系统参数
        </template>
        <template #default>
            <div class="m-4">
                <div>
                    <div class="flex flex-row justify-start items-center m-4">
                        <div class="w-20 required">键: </div>
                        <el-input class="w-80" v-model="addKey" placeholder="请输入键" />
                    </div>
                    <div class="flex flex-row justify-start items-center m-4">
                        <div class="w-20">值1: </div>
                        <el-input class="w-80" v-model="addValue1" placeholder="请输入值1" />
                        <QiniuUpload @getUrl="(url)=>{addValue1 = url}">
                            <el-button size="small" type="default" class="ml-4">上传文件<el-icon class="el-icon--right"><Upload /></el-icon></el-button>
                        </QiniuUpload>
                        <el-button size="small" type="default" class="ml-4" 
                            @click="addEditor1Show">富文本编辑器</el-button>
                    </div>
                    <div class="flex flex-row justify-start items-center m-4">
                        <div class="w-20">值2: </div>
                        <el-input class="w-80" v-model="addValue2" placeholder="请输入值2" />
                        <QiniuUpload @getUrl="(url)=>{addValue2 = url}">
                            <el-button size="small" type="default" class="ml-4">上传文件<el-icon class="el-icon--right"><Upload /></el-icon></el-button>
                        </QiniuUpload>
                        <el-button size="small" type="default" class="ml-4" 
                            @click="addEditor2Show">富文本编辑器</el-button>
                    </div>
                    <div class="flex flex-row justify-start items-center m-4">
                        <div class="w-20">描述: </div>
                        <el-input class="w-80" v-model="addDesc" placeholder="请输入描述" />
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
    <div class="m-4 flex felx-row justify-start items-center" v-permission="permissionList.selectKeyValue">
        <span class="w-20">
            键: 
        </span>
        <el-input clearable v-model="selectKey" class="w-36 mr-4"></el-input>
        <span class="w-20">
            描述: 
        </span>
        <el-input clearable v-model="selectDesc" class="w-36 mr-4"></el-input>
    </div>
    <div class="m-4 flex felx-row justify-start items-center" v-permission="permissionList.selectKeyValue">
        <el-button type="" @click="reset">重置</el-button>
        <el-button type="primary" @click="select">查询</el-button>
    </div>

    <!-- 列表 -->
    <div class="m-4" v-loading="tableLoading" v-permission="permissionList.selectKeyValue">
        <el-table
            class="h-[calc(100vh-20rem)]"
            :border="true"
            :data="pageInfo?.list"
            style="width: 100%"
            :row-key="(row:any)=>{row.id}"
        >
            <el-table-column prop="key" label="键" width="180" show-overflow-tooltip> </el-table-column>
            <el-table-column prop="value1" label="值1" width="180" show-overflow-tooltip> </el-table-column>
            <el-table-column prop="value2" label="值2" width="180" show-overflow-tooltip> </el-table-column>
            <el-table-column prop="desc" label="描述" width="180" show-overflow-tooltip> </el-table-column>
            <el-table-column fixed="right" label="操作" width="180">
                <template #default="scope">
                    <el-button type="primary" @click="updateKeyValue(scope.row)" 
                        v-permission="permissionList.editKeyValue" link>编辑</el-button>
                    <el-button type="danger" @click="deleteKeyValue(scope.row.id)" 
                        v-permission="permissionList.deleteKeyValue" link>删除</el-button>
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

    <InnerDialog v-model="updateDialogVisible" :width="800">
        <template #header>
            更新系统参数
        </template>
        <template #default>
            <div class="m-4">
                <div>
                    <div class="flex flex-row justify-start items-center m-4">
                        <div class="w-20 required">键: </div>
                        <el-input class="w-80" v-model="updateKey" placeholder="请输入键" />
                    </div>
                    <div class="flex flex-row justify-start items-center m-4">
                        <div class="w-20">值1: </div>
                        <el-input class="w-80" v-model="updateValue1" placeholder="请输入值1" />
                        <QiniuUpload @getUrl="(url)=>{updateValue1 = url}">
                            <el-button size="small" type="default" class="ml-4">上传文件<el-icon class="el-icon--right"><Upload /></el-icon></el-button>
                        </QiniuUpload>
                        <el-button size="small" type="default" class="ml-4" 
                            @click="updateEditor1Show">富文本编辑器</el-button>
                    </div>
                    <div class="flex flex-row justify-start items-center m-4">
                        <div class="w-20">值2: </div>
                        <el-input class="w-80" v-model="updateValue2" placeholder="请输入值2" />
                        <QiniuUpload @getUrl="(url)=>{updateValue2 = url}">
                            <el-button size="small" type="default" class="ml-4">上传文件<el-icon class="el-icon--right"><Upload /></el-icon></el-button>
                        </QiniuUpload>
                        <el-button size="small" type="default" class="ml-4" 
                            @click="updateEditor2Show">富文本编辑器</el-button>
                    </div>
                    <div class="flex flex-row justify-start items-center m-4">
                        <div class="w-20">描述: </div>
                        <el-input class="w-80" v-model="updateDesc" placeholder="请输入描述" />
                    </div>
                </div>
            </div>
        </template>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="updateKeyValueCancel">取消</el-button>
                <el-button type="primary" @click="updateKeyValueSave">保存</el-button>
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

<script setup lang="ts" name="KeyValue">
import InnerDialog from '@/components/utils/InnerDialog.vue'
import QiniuUpload from "@/components/utils/QiniuUpload.vue";
import WangEditor from "@/components/utils/WangEditor.vue";
import {useKeyValueHooks} from "./KeyValueHooks"

let {
    addKeyValue,
    permissionList,
    addDialogVisible,
    addKey,
    addValue1,
    addValue2,
    addDesc,
    addKeyValueCancel,
    addKeyValueSave,
    selectKey,
    selectDesc,
    reset,
    select,
    tableLoading,
    pageInfo,
    updateKeyValue,
    deleteKeyValue,
    pageRequest,
    updateDialogVisible,
    updateKey,
    updateValue1,
    updateValue2,
    updateDesc,
    updateKeyValueCancel,
    updateKeyValueSave,
    editorDialog,
    editorTarget,
    editorValue,
    editorSave,
    editorCancel,
    EditorTarget,
    addEditor1Show,
    addEditor2Show,
    updateEditor1Show,
    updateEditor2Show
} = useKeyValueHooks();

</script>

<style scoped>

</style>
