<template>
    <div class="m-4">
        <el-button type="primary" @click="addJob" v-permission="permissionList.addJob">新增</el-button>
    </div>
    <InnerDialog v-model="addDialogVisible" :width="680">
        <template #header>
            新增定时任务
        </template>
        <template #default>
            <div class="m-4">
                <div>
                    <div class="flex flex-row justify-start items-center m-4">
                        <div class="w-56 required">任务名称: </div>
                        <el-input class="w-80" v-model="addJobName" placeholder="请输入任务名称" />
                    </div>
                    <div class="flex flex-row justify-start items-center m-4">
                        <div class="w-56 required">bean名称(spring注册bean名称): </div>
                        <el-input class="w-80" v-model="addBeanName" placeholder="请输入bean名称" />
                    </div>
                    <div class="flex flex-row justify-start items-center m-4">
                        <div class="w-56 required">method名称(方法名称): </div>
                        <el-input class="w-80" v-model="addMethodName" placeholder="请输入method名称" />
                    </div>
                    <div class="flex flex-row justify-start items-center m-4">
                        <div class="w-56 required">cron表达式: </div>
                        <el-input class="w-80" v-model="addCron" placeholder="请输入cron表达式" />
                    </div>
                    <!-- <div class="flex flex-row justify-start items-center m-4">
                        <div class="w-56 required">状态: </div>
                        <el-radio-group v-model="addStatus">
                            <el-radio value="enable">启用</el-radio>
                            <el-radio value="disabled">禁用</el-radio>
                        </el-radio-group>
                    </div> -->
                </div>
            </div>
        </template>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="addJobCancel">取消</el-button>
                <el-button type="primary" @click="addJobSave">保存</el-button>
            </div>
        </template>
    </InnerDialog>

    <!-- 查询项 -->
    <div class="m-4 " v-permission="permissionList.selectJob">
        <div class="flex felx-row justify-start items-center mb-4">
            <span class="w-32">
                任务名称: 
            </span>
            <el-input clearable v-model="selectJobName" class="w-36 mr-4"></el-input>
            <span class="w-32">
                bean名称: 
            </span>
            <el-input clearable v-model="selectBeanName" class="w-36 mr-4"></el-input>
        </div>
        <div class="flex felx-row justify-start items-center">
            <span class="w-32">
                method名称: 
            </span>
            <el-input clearable v-model="selectMethodName" class="w-36 mr-4"></el-input>
            <span class="w-32">
                状态: 
            </span>
            <el-select clearable v-model="selectStatus" class="w-36 mr-4">
                <el-option label="启用" value="enable"></el-option>
                <el-option label="禁用" value="disabled"></el-option>
            </el-select>
        </div>
        
    </div>
    <div class="m-4 flex felx-row justify-start items-center" v-permission="permissionList.selectJob">
        <el-button type="" @click="reset">重置</el-button>
        <el-button type="primary" @click="select">查询</el-button>
    </div>

    <!-- 列表 -->
    <div class="m-4" v-loading="tableLoading" v-permission="permissionList.selectJob">
        <el-table
            class="h-[calc(100vh-23rem)]"
            :border="true"
            :data="pageInfo?.list"
            style="width: 100%"
            :row-key="(row:any)=>{row.id}"
        >
            <el-table-column prop="jobName" label="任务名称" width="180" show-overflow-tooltip> </el-table-column>
            <el-table-column prop="statusName" label="状态" width="80" show-overflow-tooltip>
                <template #default="scope">
                    <span v-if="scope.row.status=='enable'" class="text-green-500">{{scope.row.statusName}}</span>
                    <span v-if="scope.row.status=='disabled'" class="text-red-500">{{scope.row.statusName}}</span>
                </template>
            </el-table-column>
            <el-table-column prop="beanName" label="bean名称" width="180" show-overflow-tooltip> </el-table-column>
            <el-table-column prop="methodName" label="method名称" width="180" show-overflow-tooltip> </el-table-column>
            <el-table-column prop="cron" label="cron表达式" width="180" show-overflow-tooltip> </el-table-column>
            <el-table-column fixed="right" label="操作" width="220">
                <template #default="scope">
                    <div>
                        <el-button type="primary" 
                            @click="updateJob(scope.row.id)" v-permission="permissionList.editJob" link>编辑</el-button>
                        <el-button type="primary" v-permission="permissionList.selectJobHistory"
                            @click="selectJobHistoryShow(scope.row.id)" link>运行历史</el-button>
                        <el-button type="primary" v-permission="permissionList.actionJob"
                            @click="actionJob(scope.row.id)" link>触发任务</el-button>
                    </div>
                    <div>
                        <el-button type="primary" @click="reStart(scope.row.id)" 
                            v-permission="permissionList.restartJob" link>重启</el-button>
                        <el-button v-if="scope.row.status=='disabled'" type="success" size="default"
                            v-permission="permissionList.startJob" @click="startup(scope.row.id)" link>启用</el-button>
                        <el-button v-if="scope.row.status=='enable'" type="danger" size="default"
                            v-permission="permissionList.stopJob" @click="stop(scope.row.id)" link>禁用</el-button>
                        <el-button type="danger" 
                            @click="deleteJob(scope.row.id)" v-permission="permissionList.deleteJob" link>删除</el-button>

                    </div>
                </template>
            </el-table-column>
        </el-table>
    </div>
    <div class="m-4 flex flex-row justify-end" v-permission="permissionList.selectJob">
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

    <InnerDialog v-model="runlogDialogVisible" :width="900">
        <template #header>
            <span class="mr-2">"{{jobHistoryJob?.jobName}}"</span>
            <span class="mr-2">定时任务历史</span>
        </template>
        <template #default>
            <div class="m-4 ">
                <div class="flex felx-row justify-start items-center mb-4">
                    <span class="w-32">
                        任务启动时间:
                    </span>
                    <el-date-picker
                        v-model="selectJobHistoryDate"
                        :default-time="[new Date('2010-01-01 00:00:00'), new Date('2010-01-01 23:59:59')]"
                        value-format="YYYY-MM-DD HH:mm:ss"
                        :editable="false"
                        type="datetimerange"
                        range-separator="至"
                        start-placeholder="开始时间"
                        end-placeholder="结束时间"
                    >
                        <template #prev-month>
                            <span class="ml-2 mr-2">上月</span>
                        </template>
                        <template #next-month>
                            <span class="ml-2 mr-2">下月</span>
                        </template>
                        <template #prev-year>
                            <span class="ml-0 mr-0">上年</span>
                        </template>
                        <template #next-year>
                            <span class="ml-0 mr-0">下年</span>
                        </template>
                    </el-date-picker>
                </div>
            </div>
            <div class="m-4 ">
                <div class="flex felx-row justify-start items-center mb-4">
                    <span class="w-32">
                        状态: 
                    </span>
                    <el-select clearable v-model="selectJobHistoryStatus" class="w-36 mr-4">
                        <el-option label="运行成功" value="ok"></el-option>
                        <el-option label="运行异常" value="ex"></el-option>
                        <el-option label="运行中" value="run"></el-option>
                    </el-select>
                </div>
            </div>
            <div class="m-4 flex felx-row justify-start items-center" v-permission="permissionList.selectJobHistory">
                <el-button type="" @click="resetJobHistory">重置</el-button>
                <el-button type="primary" @click="selectJobHistory">查询</el-button>
            </div>

            <!-- 列表 -->
            <div class="m-4" v-loading="historyTableLoading" v-permission="permissionList.selectJobHistory">
                <el-table
                    class="h-[20rem]"
                    :border="true"
                    :data="historyPageInfo?.list"
                    style="width: 100%"
                    :row-key="(row:any)=>{row.id}"
                >
                    <el-table-column prop="statusName" label="状态" width="180" show-overflow-tooltip>
                        <template #default=scope>
                            <span v-if="scope.row.status=='ok'" class="text-green-500">运行成功</span>
                            <span v-if="scope.row.status=='ex'" class="text-red-500">运行异常</span>
                            <span v-if="scope.row.status=='run'" class="text-yellow-500">运行中</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="runTime" label="运行时间" width="180" show-overflow-tooltip> </el-table-column>
                    <el-table-column prop="stopTime" label="终止时间" width="180" show-overflow-tooltip> </el-table-column>
                    <el-table-column prop="exception" label="异常" width="180" show-overflow-tooltip> </el-table-column>
                </el-table>
            </div>
            <div class="m-4 flex flex-row justify-end" v-permission="permissionList.selectJobHistory">
                <el-pagination 
                    background 
                    v-model:current-page="JobHistoryPageRequest.pageNum"
                    v-model:page-size="JobHistoryPageRequest.pageSize"
                    layout="total, sizes, prev, pager, next, jumper" 
                    :page-count="Number(historyPageInfo?.totalPage)"
                    :total="Number(historyPageInfo?.total)"
                    @size-change="selectJobHistory"
                    @current-change="selectJobHistory"
                    :page-sizes="[10, 20, 50, 100, 500, 1000]"
                    prev-text="上一页"
                    next-text="下一页"
                >
                </el-pagination>
            </div>
        </template>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="jobHistoryCancel">取消</el-button>
            </div>
        </template>
    </InnerDialog>

    <InnerDialog v-model="updateDialogVisible" :width="680">
        <template #header>
            <span>更新定时任务</span>
            <div>
                <span class="text-sm text-slate-400">注意: 修改定时任务后需要重启</span>
            </div>
            
        </template>
        <template #default>
            <div class="m-4">
                <div>
                    <div class="flex flex-row justify-start items-center m-4">
                        <div class="w-56 required">任务名称: </div>
                        <el-input class="w-80" v-model="updateJobName" placeholder="请输入任务名称" />
                    </div>
                    <div class="flex flex-row justify-start items-center m-4">
                        <div class="w-56 required">bean名称(spring注册bean名称): </div>
                        <el-input class="w-80" v-model="updateBeanName" placeholder="请输入bean名称" />
                    </div>
                    <div class="flex flex-row justify-start items-center m-4">
                        <div class="w-56 required">method名称(方法名称): </div>
                        <el-input class="w-80" v-model="updateMethodName" placeholder="请输入method名称" />
                    </div>
                    <div class="flex flex-row justify-start items-center m-4">
                        <div class="w-56 required">cron表达式: </div>
                        <el-input class="w-80" v-model="updateCron" placeholder="请输入cron表达式" />
                    </div>
                    <!-- <div class="flex flex-row justify-start items-center m-4">
                        <div class="w-56 required">状态: </div>
                        <el-radio-group v-model="updateStatus">
                            <el-radio value="enable">启用</el-radio>
                            <el-radio value="disabled">禁用</el-radio>
                        </el-radio-group>
                    </div> -->
                </div>
            </div>
        </template>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="updateJobCancel">取消</el-button>
                <el-button type="primary" @click="updateJobSave">保存</el-button>
            </div>
        </template>
    </InnerDialog>
</template>

<script setup lang="ts" name="Job">
import InnerDialog from '@/components/utils/InnerDialog.vue'
import {useJobHooks} from './JobHooks'
let {
    addJob,
    permissionList,
    addDialogVisible,
    addJobName,
    addBeanName,
    addMethodName,
    addCron,
    addJobCancel,
    addJobSave,
    selectJobName,
    selectBeanName,
    selectMethodName,
    selectStatus,
    reset,
    select,
    tableLoading,
    pageRequest,
    pageInfo,
    deleteJob,
    updateJob,
    updateDialogVisible,
    updateJobName,
    updateBeanName,
    updateMethodName,
    updateCron,
    updateJobCancel,
    updateJobSave,
    startup,
    stop,
    runlogDialogVisible,
    selectJobHistoryShow,
    jobHistoryJob,
    selectJobHistoryDate,
    selectJobHistoryStatus,
    resetJobHistory,
    selectJobHistory,
    historyPageInfo,
    historyTableLoading,
    JobHistoryPageRequest,
    reStart,
    actionJob,
    jobHistoryCancel
} = useJobHooks()
</script>

<style scoped></style>
