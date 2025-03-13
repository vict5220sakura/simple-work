import {defineStore} from 'pinia'
import {ref} from 'vue'
import {defaultPath, MenuConfig, findbyPath} from '@/router/MenuConfig'
import {type PageRequest} from '@/types/PageRequest'

export const useBUserStore =  defineStore('useBUserStore', ()=>{
    
    let addDialogVisible = ref<boolean>(false)
    let username = ref<string|null>(null)
    let password = ref<string|null>(null)
    let buserCode = ref<string|null>(null)
    let selectUsername = ref<string|null>(null)
    let selectBuserCode = ref<string|null>(null)
    let roleId = ref<string|null>(null)
    
    let updateDialogVisible = ref(false)

    let updateId = ref<string>()
    let updateUsername = ref<string>()
    let updatePassword = ref<string>()
    let updateBuserCode = ref<string>()
    let updateRoleId = ref<string>()

    let pageRequest = ref<PageRequest>({
        pageNum: 1,
        pageSize: 20
    })

    return {
        addDialogVisible, 
        username, 
        password, 
        buserCode, 
        selectUsername, 
        selectBuserCode,
        pageRequest,
        roleId,
        updateDialogVisible,
        updateId,
        updateUsername,
        updatePassword,
        updateBuserCode,
        updateRoleId
    }
},{
    persist: true,
})

