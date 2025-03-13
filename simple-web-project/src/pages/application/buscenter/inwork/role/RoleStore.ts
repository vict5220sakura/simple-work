import {defineStore} from 'pinia'
import {ref} from 'vue'
import {defaultPath, MenuConfig, findbyPath} from '@/router/MenuConfig'
import {type PageRequest} from '@/types/PageRequest'

export const useRoleStore =  defineStore('useRoleStore', ()=>{
    
    let addDialogVisible = ref<boolean>(false)
    let rolename = ref<string>()
    let pageRequest = ref<PageRequest>({
        pageNum: 1,
        pageSize: 20
    })
    let permissionList = ref<string[]>()
    let selectRolename = ref<string>()
    
    let updateDialogVisible = ref(false)
    let updateRolename = ref<string>()
    let updatePermissionList = ref<string[]>()
    let updateId = ref<string>()

    return {
        addDialogVisible, 
        pageRequest,
        rolename,
        permissionList,
        selectRolename,
        updateDialogVisible,
        updateRolename,
        updatePermissionList,
        updateId
    }
},{
    persist: true,
})

