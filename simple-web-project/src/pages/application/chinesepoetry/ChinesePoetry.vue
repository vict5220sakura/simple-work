<template>
    <div class="flex flex-row justify-start items-start">
        <!-- 左侧栏目 -->
        <div class="h-[calc(100vh-48px-30px)] w-80 p-4">
            <el-card class="h-[calc(100vh-48px-30px-60px)] w-80" shadow="hover" 
                v-loading="chinesePoetryClassifyListLoading">
                <el-scrollbar class="h-[calc(100vh-48px-30px-80px)]">
                    <div v-for="(item,index) in chinesePoetryClassifyList" :key="item.id"
                        @click="selectChinesePoetryClassify(item)"
                        :class="['select-none', 'cursor-pointer', 'hover:border-gray-300', 
                            'border-2', 'border-white', 
                            'rounded-sm', 'p-2', item.id == selectChinesePoetryClassifyId?'bg-sky-400':'',
                            'flex', 'flex-row', 'justify-between', 'items-center']">
                        <span class="w-40">{{item.classifyName}}</span>
                        <el-button v-if="deleteChinesePoetryClassifyFlag && item.classifyName!='其他'" type="danger" 
                            link @click="deleteChinesePoetryClassify(item.id)">删除</el-button>
                        <el-button v-if="updateChinesePoetryClassifyFlag && item.classifyName!='其他'" type="primary" 
                            link @click="willUpdateChinesePoetryClassify(item.id)">修改</el-button>
                        <div class="">
                            <el-button v-if="moveChinesePoetryClassifyFlag" 
                                size="small" @click="classifyMoveUp(item.id)"><svg t="1740122814829" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2343" width="16" height="16"><path d="M325.450697 862.306736" fill="#1296db" p-id="2344"></path><path d="M882.088359 862.306736" fill="#1296db" p-id="2345"></path><path d="M236.00336 877.09995" fill="#1296db" p-id="2346"></path><path d="M960.182765 877.09995" fill="#1296db" p-id="2347"></path><path d="M63.645221 788.684697" fill="#1296db" p-id="2348"></path><path d="M958.462624 788.684697" fill="#1296db" p-id="2349"></path><path d="M64.84932 858.69444" fill="#1296db" p-id="2350"></path><path d="M959.494709 858.69444" fill="#1296db" p-id="2351"></path><path d="M842.009071 396.492525l-296.036284-295.86427c-18.749538-18.749538-49.196036-18.749538-67.945574 0l-295.86427 296.036284c-26.662187 26.662187-4.472367 73.278011 30.446498 73.278011l146.728036 0 0 420.5745c0 25.974131 20.985721 47.131866 47.131866 47.131866l211.233328 0c25.974131 0 47.131866-20.985721 47.131866-47.131866L664.834537 469.770536 811.906602 469.770536C847.513523 469.770536 867.811188 422.63867 842.009071 396.492525z" fill="#1296db" p-id="2352"></path></svg></el-button>
                            <el-button v-if="moveChinesePoetryClassifyFlag" 
                                size="small" @click="classifyMoveDown(item.id)"><svg t="1740122828540" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2316" width="16" height="16"><path d="M325.450697 862.306736" fill="#1296db" p-id="2317"></path><path d="M882.088359 862.306736" fill="#1296db" p-id="2318"></path><path d="M236.00336 877.09995" fill="#1296db" p-id="2319"></path><path d="M960.182765 877.09995" fill="#1296db" p-id="2320"></path><path d="M63.645221 788.684697" fill="#1296db" p-id="2321"></path><path d="M958.462624 788.684697" fill="#1296db" p-id="2322"></path><path d="M64.84932 858.69444" fill="#1296db" p-id="2323"></path><path d="M959.494709 858.69444" fill="#1296db" p-id="2324"></path><path d="M840.28893 627.335461l-296.036284 295.86427c-18.749538 18.749538-49.196036 18.749538-67.945574 0l-295.86427-296.036284c-26.662187-26.662187-4.472367-73.278011 30.446498-73.278011l146.728036 0L357.617336 133.48295c0-25.974131 20.985721-47.131866 47.131866-47.131866l211.233328 0c25.974131 0 47.131866 20.985721 47.131866 47.131866l0 420.5745L810.186461 554.05745C845.793381 554.05745 866.091047 601.36133 840.28893 627.335461z" fill="#1296db" p-id="2325"></path></svg></el-button>
                        </div>
                    </div>
                    <div class="w-full flex justify-center flex-row p-2">
                        <el-button @click="addChinesePoetryClassify">新增类目</el-button></div>
                </el-scrollbar>
            </el-card>
            <div class="h-[30px] flex flex-row justify-start items-center select-none">
                <el-checkbox v-permission="permissionList.chinesePoetryClassifyDelete" v-model="deleteChinesePoetryClassifyFlag">
                    <span class="text-gray-500">&nbsp;删除类目</span></el-checkbox> 

                <el-checkbox v-permission="permissionList.chinesePoetryClassifyUpdate" v-model="updateChinesePoetryClassifyFlag">
                    <span class="text-gray-500">&nbsp;修改类目</span></el-checkbox>

                <el-checkbox v-permission="permissionList.chinesePoetryClassifyMove" v-model="moveChinesePoetryClassifyFlag">
                    <span class="text-gray-500">&nbsp;编辑顺序</span></el-checkbox>
            </div>
        </div>

        <!-- 右侧栏目 -->
        <div v-if="selectChinesePoetryClassifyId != undefined && selectChinesePoetryClassifyId != null" class="h-[calc(100vh-48px-30px)] pl-8">
            <!-- 操作栏 -->
            <div class="flex flex-col ">
                <div class="h-[40px] pt-8 flex felx-row justify-start items-center">
                    <el-button v-permission="permissionList.chinesePoetryAdd" type="primary" @click="willAddChinesePoetry">新增古诗</el-button>
                </div>
                <div class="h-[40px] pt-8 flex felx-row justify-start items-center">
                    <span class="w-20">
                        标题: 
                    </span>
                    <el-input clearable v-model="selectTitle" class="w-36 mr-4"></el-input>
                    <span class="w-20">
                        作者: 
                    </span>
                    <el-input clearable v-model="selectAuthor" class="w-36 mr-4"></el-input>
                </div>
                <div class="h-[40px] pt-10 flex felx-row justify-between items-center">
                    <div class="">
                        <el-button v-permission="permissionList.chinesePoetry" type="" @click="reset">重置</el-button>
                        <el-button v-permission="permissionList.chinesePoetry" type="primary" @click="select">查询</el-button>
                    </div>
                    <div class="flex flex-row justify-start items-center">
                        <el-button v-permission="permissionList.chinesePoetryImport" type="" @click="willImportData">导入数据</el-button>
                        <el-button v-permission="permissionList.chinesePoetryChangeClassify" type="" @click="willChangeChinesePoetryClassify">修改类目</el-button>
                        <el-button v-permission="permissionList.chinesePoetryDelete" type="danger" @click="willDeleteMany">批量删除</el-button>
                    </div>
                </div>
                
            </div>
            <!-- 列表 -->
            <div class="h-[calc(100vh-48px-30px-40px-40px-40px)] pt-8 flex flex-col justify-between ">
                <div class="" v-loading="tableLoading">
                    <el-table
                        ref="tableRef"
                        class="h-[calc(100vh-48px-30px-40px-40px-40px-80px-20px)]"
                        :border="true"
                        :data="pageInfo?.list"
                        style="width: 100%"
                        :row-key="(row:any)=>{row.id}"
                    >
                        <el-table-column type="selection" width="45" />
                        <el-table-column prop="title" label="标题" width="140" show-overflow-tooltip> </el-table-column>
                        <el-table-column prop="author" label="作者" width="140" show-overflow-tooltip> </el-table-column>
                        <el-table-column label="内容" width="260">
                            <template #default="scope">
                                <!-- <el-tooltip
                                    effect="write"
                                    placement="top"
                                >
                                    
                                </el-tooltip> -->
                                <el-tooltip
                                    :content="formatParagraphs(scope.row.paragraphs, false)"
                                    raw-content
                                    effect="dark"
                                    placement="top-start"
                                >
                                    <div v-html="formatParagraphs(scope.row.paragraphs, true)"></div>
                                </el-tooltip>
                                <!-- {{formatParagraphs(scope.row.paragraphs)}} -->
                            </template>
                        </el-table-column>
                        <el-table-column fixed="right" label="操作" width="200">
                            <template #default="scope">
                                <el-button type="primary" @click="willUpdateChinesePoetry(scope.row)" 
                                v-permission="permissionList.chinesePoetryUpdate" link>编辑</el-button>
                                <el-button type="danger" @click="willDeleteChinesePoetry(scope.row.id)" 
                                v-permission="permissionList.chinesePoetryDelete" link>删除</el-button>
                                <br>
                                <div class="pt-2">
                                    <el-button v-permission="permissionList.chinesePoetryMove" size="small" @click="moveUp(scope.row.id)"><svg t="1740122814829" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2343" width="16" height="16"><path d="M325.450697 862.306736" fill="#1296db" p-id="2344"></path><path d="M882.088359 862.306736" fill="#1296db" p-id="2345"></path><path d="M236.00336 877.09995" fill="#1296db" p-id="2346"></path><path d="M960.182765 877.09995" fill="#1296db" p-id="2347"></path><path d="M63.645221 788.684697" fill="#1296db" p-id="2348"></path><path d="M958.462624 788.684697" fill="#1296db" p-id="2349"></path><path d="M64.84932 858.69444" fill="#1296db" p-id="2350"></path><path d="M959.494709 858.69444" fill="#1296db" p-id="2351"></path><path d="M842.009071 396.492525l-296.036284-295.86427c-18.749538-18.749538-49.196036-18.749538-67.945574 0l-295.86427 296.036284c-26.662187 26.662187-4.472367 73.278011 30.446498 73.278011l146.728036 0 0 420.5745c0 25.974131 20.985721 47.131866 47.131866 47.131866l211.233328 0c25.974131 0 47.131866-20.985721 47.131866-47.131866L664.834537 469.770536 811.906602 469.770536C847.513523 469.770536 867.811188 422.63867 842.009071 396.492525z" fill="#1296db" p-id="2352"></path></svg></el-button>
                                    <el-button v-permission="permissionList.chinesePoetryMove" size="small" @click="moveDown(scope.row.id)"><svg t="1740122828540" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2316" width="16" height="16"><path d="M325.450697 862.306736" fill="#1296db" p-id="2317"></path><path d="M882.088359 862.306736" fill="#1296db" p-id="2318"></path><path d="M236.00336 877.09995" fill="#1296db" p-id="2319"></path><path d="M960.182765 877.09995" fill="#1296db" p-id="2320"></path><path d="M63.645221 788.684697" fill="#1296db" p-id="2321"></path><path d="M958.462624 788.684697" fill="#1296db" p-id="2322"></path><path d="M64.84932 858.69444" fill="#1296db" p-id="2323"></path><path d="M959.494709 858.69444" fill="#1296db" p-id="2324"></path><path d="M840.28893 627.335461l-296.036284 295.86427c-18.749538 18.749538-49.196036 18.749538-67.945574 0l-295.86427-296.036284c-26.662187-26.662187-4.472367-73.278011 30.446498-73.278011l146.728036 0L357.617336 133.48295c0-25.974131 20.985721-47.131866 47.131866-47.131866l211.233328 0c25.974131 0 47.131866 20.985721 47.131866 47.131866l0 420.5745L810.186461 554.05745C845.793381 554.05745 866.091047 601.36133 840.28893 627.335461z" fill="#1296db" p-id="2325"></path></svg></el-button>
                                </div>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>
                <!-- 分页 -->
                <div class="m-4 flex flex-row justify-end">
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
            </div>
        </div>

    </div>

    <InnerDialog v-model="addChinesePoetryClassifyDialogVisible" :width="500">
        <template #header>
            新增类目
        </template>
        <template #default>
            <div>
                <div class="flex flex-row justify-start items-center m-4">
                    <div class="w-20 required flex justify-end">类目名称:&nbsp;&nbsp;</div>
                    <el-input class="w-80" v-model="addChinesePoetryClassifyName" placeholder="请输入类目名称" />
                </div>
            </div>
        </template>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="addChinesePoetryClassifyCancel">取消</el-button>
                <el-button type="primary" @click="addChinesePoetryClassifySave">保存</el-button>
            </div>
        </template>
    </InnerDialog>


    <InnerDialog v-model="updateChinesePoetryClassifyDialogVisible" :width="500">
        <template #header>
            修改类目
        </template>
        <template #default>
            <div>
                <div class="flex flex-row justify-start items-center m-4">
                    <div class="w-20 required flex justify-end">类目名称:&nbsp;&nbsp;</div>
                    <el-input class="w-80" v-model="updateChinesePoetryClassifyName" placeholder="请输入类目名称" />
                </div>
            </div>
        </template>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="updateChinesePoetryClassifyCancel">取消</el-button>
                <el-button type="primary" @click="updateChinesePoetryClassifySave">保存</el-button>
            </div>
        </template>
    </InnerDialog>

    <InnerDialog v-model="addChinesePoetryDialogVisible" :width="500">
        <template #header>
            新增古诗
        </template>
        <template #default>
            <div>
                <div class="flex flex-row justify-start items-center m-4">
                    <div class="w-20 required flex justify-end">标题:&nbsp;&nbsp;</div>
                    <el-input clearable class="w-80" v-model="addChinesePoetryTitle" placeholder="请输入标题" />
                </div>
                <div class="flex flex-row justify-start items-center m-4">
                    <div class="w-20 required flex justify-end">作者:&nbsp;&nbsp;</div>
                    <el-input clearable class="w-80" v-model="addChinesePoetryAuthor" placeholder="请输入作者" />
                </div>
                <div class="flex flex-row justify-start items-center m-4">
                    <div class="w-20 required flex justify-end">内容:&nbsp;&nbsp;</div>
                    <el-input clearable :rows="10" type="textarea" class="w-80" v-model="addChinesePoetryParagraphs" placeholder="请输入内容" />
                </div>
                <div class="w-[25rem] flex flex-row justify-end items-center m-4">
                    <el-button size="small" @click="cleanAddChinesePoetry">清空</el-button>
                </div>
            </div>
        </template>
        <template #footer>
            <div class="">
                <el-button @click="addChinesePoetryCancel">取消</el-button>
                <el-button type="primary" @click="addChinesePoetrySave">保存</el-button>
            </div>
        </template>
    </InnerDialog>

    <InnerDialog v-model="updateChinesePoetryDialogVisible" :width="500">
        <template #header>
            编辑古诗
        </template>
        <template #default>
            <div>
                <div class="flex flex-row justify-start items-center m-4">
                    <div class="w-20 required flex justify-end">标题:&nbsp;&nbsp;</div>
                    <el-input clearable class="w-80" v-model="updateChinesePoetryTitle" placeholder="请输入标题" />
                </div>
                <div class="flex flex-row justify-start items-center m-4">
                    <div class="w-20 required flex justify-end">作者:&nbsp;&nbsp;</div>
                    <el-input clearable class="w-80" v-model="updateChinesePoetryAuthor" placeholder="请输入作者" />
                </div>
                <div class="flex flex-row justify-start items-center m-4">
                    <div class="w-20 required flex justify-end">内容:&nbsp;&nbsp;</div>
                    <el-input clearable :rows="10" type="textarea" class="w-80" v-model="updateChinesePoetryParagraphs" placeholder="请输入内容" />
                </div>
            </div>
        </template>
        <template #footer>
            <div class="">
                <el-button @click="updateChinesePoetryCancel">取消</el-button>
                <el-button type="primary" @click="updateChinesePoetrySave">保存</el-button>
            </div>
        </template>
    </InnerDialog>

    <InnerDialog v-model="changeChinesePoetryClassifyDialogVisible">
        <template #header>
            修改类目
        </template>
        <template #default>
            <div>
                <div class="flex flex-row justify-start items-center m-4">
                    <div class="w-20 required flex justify-end">类目:&nbsp;&nbsp;</div>
                    <el-select v-model="changeChinesePoetryClassifyId">
                        <el-option v-for="item in chinesePoetryClassifyList" 
                            :disabled="item.id == selectChinesePoetryClassifyId"
                            :key="item.id" :label="item.classifyName" :value="item.id">{{ item.classifyName }}</el-option>
                    </el-select>
                </div>
            </div>
        </template>
        <template #footer>
            <div class="">
                <el-button @click="changeChinesePoetryClassifyCancel">取消</el-button>
                <el-button type="primary" @click="changeChinesePoetryClassifySave">保存</el-button>
            </div>
        </template>
    </InnerDialog>
    
    <InnerDialog v-model="importDataDialogVisible">
        <template #header>
            导入数据
        </template>
        <template #default>
            <div>
                <div class="flex flex-row justify-start items-center m-4">
                    <div class="w-20 required flex justify-end">选择文件:&nbsp;&nbsp;</div>
                    <QiniuUpload @getUrl="(url)=>{importDataFileUrl = url}">
                        <el-button size="small" type="default" class="ml-4">选择文件<el-icon class="el-icon--right"><Upload /></el-icon></el-button>
                    </QiniuUpload>
                    <span class="pl-4 text-xs self-end text-slate-500">{{urlGetFileName(importDataFileUrl)}}</span>
                </div>
                <div class="flex flex-row justify-start items-center m-4">
                    <div class="w-20 required flex justify-end">输入数据:&nbsp;&nbsp;</div>
                    <el-input type="textarea" :rows="10" clearable class="w-80 ml-4" 
                        v-model="importData" placeholder="请输入数据" />
                </div>
            </div>
        </template>
        <template #footer>
            <div class="">
                <el-button @click="importDataCancel">取消</el-button>
                <el-button type="primary" @click="importDataSave">保存</el-button>
            </div>
        </template>
    </InnerDialog>
</template>

<script setup lang="ts" name="ChinesePoetry">
import InnerDialog from '@/components/utils/InnerDialog.vue'
import QiniuUpload from '@/components/utils/QiniuUpload.vue'
import {useChinesePoetryHooks} from "@/pages/application/chinesepoetry/ChinesePoetryHooks";
import { Select } from '@element-plus/icons-vue';
let{
    permissionList,
    chinesePoetryClassifyList,
    addChinesePoetryClassifyDialogVisible,
    addChinesePoetryClassify,
    addChinesePoetryClassifyName,
    addChinesePoetryClassifyCancel,
    addChinesePoetryClassifySave,
    chinesePoetryClassifyListLoading,
    selectChinesePoetryClassify,
    deleteChinesePoetryClassifyFlag,
    deleteChinesePoetryClassify,
    selectChinesePoetryClassifyId,

    // 修改类目
    updateChinesePoetryClassifyFlag,
    willUpdateChinesePoetryClassify,
    updateChinesePoetryClassifyDialogVisible,
    updateChinesePoetryClassifyName,
    updateChinesePoetryClassifyCancel,
    updateChinesePoetryClassifySave,

    // 移动类目
    moveChinesePoetryClassifyFlag,
    classifyMoveUp,
    classifyMoveDown,

    // 列表
    tableLoading,
    pageInfo,
    pageRequest,
    select,
    reset,
    selectTitle,
    selectAuthor,
    moveUp,
    moveDown,

    willAddChinesePoetry,
    addChinesePoetryDialogVisible,
    addChinesePoetryTitle,
    addChinesePoetryAuthor,
    addChinesePoetryParagraphs,
    addChinesePoetryCancel,
    addChinesePoetrySave,
    cleanAddChinesePoetry,

    // 删除
    willDeleteChinesePoetry,
    willDeleteMany,

    // 更新
    willUpdateChinesePoetry,
    updateChinesePoetryDialogVisible,
    updateChinesePoetryId,
    updateChinesePoetryTitle,
    updateChinesePoetryAuthor,
    updateChinesePoetryParagraphs,
    updateChinesePoetryCancel,
    updateChinesePoetrySave,

    // 修改类目
    willChangeChinesePoetryClassify,
    changeChinesePoetryClassifyDialogVisible,
    changeChinesePoetryClassifyId,
    changeChinesePoetryClassifyCancel,
    changeChinesePoetryClassifySave,

    // 导入
    willImportData,
    importDataDialogVisible,
    importDataFileUrl,
    urlGetFileName,
    importDataCancel,
    importDataSave,
    importData,

    formatParagraphs,
} = useChinesePoetryHooks()
</script>

<style scoped>
.fullHeight{
    height: calc(100vh - 48px - 30px)
} 
</style>
