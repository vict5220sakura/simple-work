import {defineStore} from 'pinia'
import {ref} from 'vue'

export const usePermissionStore =  defineStore('permissionStore', ()=>{
    let permissionList = ref<string[]|null>()
    let hasPermission = (permission:string):boolean=>{
        if(permissionList.value == null){
            return false;
        }
        return permissionList.value.includes(permission)
    }
    return {permissionList, hasPermission}
},{
    persist: true,
})

