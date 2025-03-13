
import {permissionList} from "@/config/PermissionConfig";
import {useJobStore} from "@/pages/application/system/job/JobStore"
import {storeToRefs} from "pinia"
import {request} from "@/utils/Request"
import type {R} from "@/types/R";
import { ElMessage, ElMessageBox } from "element-plus";
import {ref, onMounted} from 'vue'
import type {PageInfo} from "@/types/PageInfo";
import {type Job, type JobHistory} from "./JobTypes"


const jobStore = useJobStore()
let {
    addDialogVisible,
    addJobName,
    addBeanName,
    addMethodName,
    addCron,
    selectJobName,
    selectBeanName,
    selectMethodName,
    selectStatus,
    pageRequest,
    updateDialogVisible,
    updateId,
    updateJobName,
    updateBeanName,
    updateMethodName,
    updateCron,
    runlogDialogVisible,
    jobHistoryJob,
    selectJobHistoryDate,
    selectJobHistoryStatus,
    JobHistoryPageRequest
} = storeToRefs(jobStore)

export const useJobHooks = function(){
    const addJob = ()=>{
        addDialogVisible.value = true
    }
    const addJobCancel = ()=>{
        addDialogVisible.value = false
    }
    const addJobSave = async()=>{
        let r:R<any> = await request.post("/job/insertJob", {
            jobName: addJobName.value,
            beanName: addBeanName.value,
            methodName: addMethodName.value,
            cron: addCron.value,
            // status: addStatus.value
        })
        if(r.code === "success"){
            addDialogVisible.value = false
            ElMessage.success("新增定时任务成功")
            select()
        }
    }

    let tableLoading = ref<boolean>(false)

    // 初始化查询一次
    onMounted(()=>{
        select()
        selectJobHistoryDate.value = undefined
        selectJobHistoryStatus.value = undefined
        selectJobHistory()
    })

    const select = async ()=>{
        tableLoading.value = true;
        let r:R<PageInfo<Job>> = await request.post("/job/jobList", {
            jobName: selectJobName.value,
            beanName: selectBeanName.value,
            methodName: selectMethodName.value,
            status: selectStatus.value,
            pageNum: pageRequest.value.pageNum,
            pageSize: pageRequest.value.pageSize
        })
        tableLoading.value = false;
        if(r.code == "success"){
            pageInfo.value = r.data
        }
    }

    // 响应参数
    let pageInfo = ref<PageInfo<any>>()

    const reset = ()=>{
        selectJobName.value = undefined
        selectBeanName.value = undefined
        selectMethodName.value = undefined
        selectStatus.value = undefined
        select()
    }

    /** 删除 */
    const deleteJob = async(id:string)=> {
        ElMessageBox.confirm(
            '是否确认删除?',
            '是否确认删除?',
            {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
            }
        ).then(async() => {
            let r:R<any> = await request.post("/job/deleteJobById", {
                id: id
            })
            if(r.code == "success"){
                ElMessage.success("删除成功")
                select();
            }
            
        }).catch(() => {
            
        })
    }

    const updateJob = async(id:string)=>{
        let r:R<Job> = await request.post("/job/jobInfo", {
            id: id
        })
        let data = r.data
        updateJobName.value = data.jobName
        updateBeanName.value = data.beanName
        updateMethodName.value = data.methodName
        updateCron.value = data.cron
        // updateStatus.value = data.status
        updateId.value = id
        updateDialogVisible.value = true
    }

    const updateJobCancel = ()=>{
        updateDialogVisible.value = false
    }

    const updateJobSave = async ()=>{
        let r:R<any> = await request.post("/job/updateJobById", {
            id: updateId.value,
            jobName: updateJobName.value,
            beanName: updateBeanName.value,
            methodName: updateMethodName.value,
            cron: updateCron.value,
            // status: updateStatus.value
        })
        if(r.code === "success"){
            updateDialogVisible.value = false
            ElMessage.success("更新定时任务成功")
            select()
        }


        updateDialogVisible.value = false
        select()
    }

    const startup = async (id:string)=>{
        let r:R<any> = await request.post("/job/startup", {
            id: id,
        })
        if(r.code === "success"){
            ElMessage.success("启用成功")
            select()
        }
    }

    const stop = async (id:string)=>{
        let r:R<any> = await request.post("/job/stop", {
            id: id,
        })
        if(r.code === "success"){
            ElMessage.success("已禁用")
            select()
        }
    }

    const selectJobHistoryShow = async (id:string)=>{
        let r:R<Job> = await request.post("/job/jobInfo", {
            id: id
        })
        jobHistoryJob.value = r.data
        selectJobHistoryDate.value = undefined
        selectJobHistoryStatus.value = undefined
        runlogDialogVisible.value = true
        selectJobHistory()
    }

    let historyPageInfo = ref<PageInfo<JobHistory>>()
    let historyTableLoading = ref<boolean>(false)

    const selectJobHistory = async ()=>{
        historyTableLoading.value = true;
        let r:R<PageInfo<JobHistory>> = await request.post("/job/jobHistoryList", {
            startTime: selectJobHistoryDate.value?.[0],
            endTime: selectJobHistoryDate.value?.[1],
            status: selectJobHistoryStatus.value,
            pageNum: JobHistoryPageRequest.value.pageNum,
            pageSize: JobHistoryPageRequest.value.pageSize
        })
        historyTableLoading.value = false;
        if(r.code == "success"){
            historyPageInfo.value = r.data
        }
    }

    const resetJobHistory = ()=>{
        selectJobHistoryDate.value = undefined;
        selectJobHistoryStatus.value = undefined;
        selectJobHistory()
    }

    const reStart = async (id:string)=>{
        let r:R<any> = await request.post("/job/reStart", {
            id: id,
        })
        if(r.code === "success"){
            ElMessage.success("重启成功")
            select()
        }
    }

    const actionJob = async (id:string)=>{
        let r:R<any> = await request.post("/job/actionJob", {
            id: id,
        })
        if(r.code === "success"){
            ElMessage.success("触发成功")
            select()
        }
    }
    const jobHistoryCancel = ()=>{
        runlogDialogVisible.value = false
    }

    return {
        addJob,
        permissionList,
        addDialogVisible,
        addJobName,
        addBeanName,
        addMethodName,
        // addStatus,
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
        // updateStatus,
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
    }
}