import {useCustomerOptionStore} from '@/pages/application/customer/CustomerOptionStore'
import {storeToRefs} from 'pinia'
import {type R} from "@/types/R"
import {request} from '@/utils/Request'
import {ElMessage, ElMessageBox} from "element-plus"
import {ref, onMounted} from 'vue'
import {type PageInfo} from "@/types/PageInfo"
import type {CustomerListItem} from "@/pages/application/customer/CustomerTypes"

let customerOptionStore = useCustomerOptionStore()
let {
    addDialogVisible,
    addCustomerPhone,
    addCustomerName,
    selectCustomerPhone,
    selectCustomerName,
    pageRequest,
    updateId,
    updateCustomerPhone,
    updateCustomerName,
    updateDialogVisible
} = storeToRefs(customerOptionStore)

export const useCustomerOptionHooks = function(){
    const addCustomer = ()=>{
        addDialogVisible.value = true
    }
    const addKeyValueCancel = ()=>{
        addDialogVisible.value = false
    }
    const addKeyValueSave = async ()=>{
        let r:R<any> = await request.post("/customer/insertCustomer", {
            customerPhone: addCustomerPhone.value,
            customerName: addCustomerName.value
        })
        if(r.code === "success"){
            addDialogVisible.value = false
            ElMessage.success("新增系统参数成功")
            select()
        }
    }

    // 查询
    // 响应参数
    let pageInfo = ref<PageInfo<CustomerListItem>>()
    let tableLoading = ref<boolean>(false)
    const select = async ()=>{
        tableLoading.value = true;
        let r:R<any> = await request.post("/customer/customerList", {
            customerName: selectCustomerName.value,
            customerPhone: selectCustomerPhone.value,
            pageNum: pageRequest.value.pageNum,
            pageSize: pageRequest.value.pageSize
        })
        
        if(r.code == "success"){
            pageInfo.value = r.data
        }
        tableLoading.value = false;
    }

    // 初始化查询一次
    onMounted(()=>{
        select()
    })

    const reset = ()=>{
        selectCustomerPhone.value = null
        selectCustomerName.value = null
        select()
    }

    /** 删除 */
    const deleteCustomer = async(id:string)=> {
        ElMessageBox.confirm(
            '是否确认删除?',
            '是否确认删除?',
            {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
            }
        ).then(async() => {
            let r:R<any> = await request.post("/customer/deleteCustomer", {
                id: id
            })
            if(r.code == "success"){
                ElMessage.success("删除成功")
                select();
            }
            
        }).catch(() => {
            
        })
    }

    /** 重置密码 */
    const resetPassword = (id:string)=>{
        ElMessageBox.confirm(
            '密码将会被重置为(123456)是否确认重置密码?',
            '是否确认重置密码?',
            {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
            }
        ).then(async() => {
            let r:R<any> = await request.post("/customer/resetPassword", {
                id: id
            })
            if(r.code == "success"){
                ElMessage.success("重置密码成功")
                // select();
            }
            
        }).catch(() => {
            
        })
    }

    // 编辑
    const updateCustomer = (row:any)=>{
        updateId.value = row.id

        updateCustomerPhone.value = row.customerPhone
        updateCustomerName.value = row.customerName
        updateDialogVisible.value = true
    }

    const updateCustomerCancel = ()=>{
        updateDialogVisible.value = false
    }

    const updateCustomerSave = async ()=>{
        let r:R<any> = await request.post("/customer/updateCustomer", {
            id: updateId.value,
            customerPhone: updateCustomerPhone.value,
            customerName: updateCustomerName.value
        })
        if(r.code === "success"){
            updateDialogVisible.value = false
            ElMessage.success("编辑客户成功")
            select()
        }
    }

    return {
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
    }
}