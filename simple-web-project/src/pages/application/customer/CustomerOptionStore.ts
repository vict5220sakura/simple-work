import {defineStore} from 'pinia'
import {ref} from 'vue'
import {type PageRequest} from "@/types/PageRequest"

export const useCustomerOptionStore =  defineStore('CustomerOptionStore', ()=>{
    let addDialogVisible = ref<boolean>(false)
    let addCustomerPhone = ref<string>();
    let addCustomerName = ref<string>();

    let selectCustomerPhone = ref<string|null>();
    let selectCustomerName = ref<string|null>();
    
    let pageRequest = ref<PageRequest>({
        pageNum: 1,
        pageSize: 20
    })

    let updateId = ref<string|null>()
    let updateCustomerPhone = ref<string|null>()
    let updateCustomerName = ref<string|null>()
    let updateDialogVisible = ref<boolean>(false)

    return {
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
    }
},{
    persist: true,
})
