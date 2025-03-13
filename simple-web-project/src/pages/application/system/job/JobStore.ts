import {defineStore} from 'pinia'
import {ref} from 'vue'
import {type PageRequest} from '@/types/PageRequest'
import type {Job} from "./JobTypes"

export const useJobStore =  defineStore('JobStore', ()=>{
    let addDialogVisible = ref<boolean>(false)
    let addJobName = ref<string>()
    let addBeanName = ref<string>()
    let addMethodName = ref<string>()
    let addCron = ref<string>()
    // let addStatus = ref<string>()

    let selectJobName = ref<string>()
    let selectBeanName = ref<string>()
    let selectMethodName = ref<string>()
    let selectStatus = ref<string>()

    let pageRequest = ref<PageRequest>({
        pageNum: 1,
        pageSize: 20
    })

    let updateDialogVisible = ref<boolean>(false)
    let updateId = ref<string>()
    let updateJobName = ref<string>()
    let updateBeanName = ref<string>()
    let updateMethodName = ref<string>()
    let updateCron = ref<string>()
    // let updateStatus = ref<string>()

    let runlogDialogVisible = ref<boolean>(false)

    let jobHistoryJob = ref<Job>()

    let selectJobHistoryDate = ref<any[]>()
    let selectJobHistoryStatus = ref<string>()

    let JobHistoryPageRequest = ref<PageRequest>({
        pageNum: 1,
        pageSize: 20
    })

    return {
        addDialogVisible,
        addJobName,
        addBeanName,
        addMethodName,
        addCron,
        // addStatus,
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
        // updateStatus
        runlogDialogVisible,
        jobHistoryJob,
        selectJobHistoryDate,
        selectJobHistoryStatus,
        JobHistoryPageRequest
    }
},{
    persist: true,
})

