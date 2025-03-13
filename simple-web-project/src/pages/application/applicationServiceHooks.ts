import { ElMessage, ElMessageBox } from "element-plus"
import {useLoginStore} from "@/pages/login/LoginStore"
import { storeToRefs } from 'pinia';
import {router} from "@/router/Router"
import { useApplicationStore } from '@/pages/application/applicationStore'
import {useTagBarStore} from "@/components/headtag/TagBarStore";
import type { R } from "@/types/R";
import { request } from "@/utils/Request";
import {usePermissionStore} from "@/components/role/permissionStore"

export const useApplicationServiceHooks = ()=>{
    let {token} = storeToRefs(useLoginStore())

    let applicationStore = useApplicationStore()
    let tagBarStore = useTagBarStore()

    let permissionStore = usePermissionStore()
    let {permissionList} = storeToRefs(permissionStore)

    // 登出
    function loginOut(){
        ElMessageBox.confirm(
            '是否确认登出?',
            '是否确认登出?',
            {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
            }
        ).then(async() => {

            token.value = undefined

            router.push({
                path: "/login"
            })
        }).catch(() => {
            
        })
    }

    /** 重载缓存 */
    function reloadCache(){
        applicationStore.reload();
        tagBarStore.reload();
    }

    // 清除缓存功能
    function clearCache(){
        reloadCache()
        // window.localStorage.clear()
        
        ElMessageBox.confirm(
            '是否刷新页面?',
            '清除缓存成功!',
            {
            confirmButtonText: '刷新页面',
            cancelButtonText: '不刷新',
            type: 'warning',
            }
        ).then(() => {
            location.reload()
        }).catch(() => {
            
        })
    }

    /** 刷新权限 */
    async function flushPermission(){
        let r:R<any> = await request.post("/bUserLogin/selectPermission")
        if(r.code == "success"){
            
            permissionList.value = r.data.permissionList
    
            ElMessage.success("刷新权限成功")
        }
    }

    return {loginOut, reloadCache, clearCache, flushPermission} 
}