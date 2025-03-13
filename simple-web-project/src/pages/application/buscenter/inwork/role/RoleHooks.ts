import { ref, onMounted } from 'vue';
import {emitter} from '@/utils/Emitter'
import { storeToRefs } from 'pinia';
import {request} from '@/utils/Request'
import { ElMessage, ElMessageBox } from 'element-plus';
import type {R} from "@/types/R"
import type {PageInfo} from '@/types/PageInfo'
import type {PageRequest} from '@/types/PageRequest'
import {md5} from "js-md5";
import {Optional} from '@/utils/Optional'
import { useRoleStore } from './RoleStore'


export const useRoleHooks = function(){
    /** 存储 */
    let roleStore = useRoleStore()
    let {
        addDialogVisible, 
        rolename,
        permissionList,
        selectRolename,
        pageRequest,
        updateDialogVisible,
        updateRolename,
        updatePermissionList,
        updateId
    } = storeToRefs(roleStore)

    /** 新增 */
    let addRole = ()=>{
        addDialogVisible.value = true
    }
    let addRoleCancel = ()=>{
        addDialogVisible.value = false
    }
    let addRoleSave = async()=>{
        
        let r:R<any> = await request.post("/role/addRole", {
            rolename: rolename.value,
            permissionList: permissionList.value
        })
        if(r.code === "success"){
            addDialogVisible.value = false
            ElMessage.success("新增角色成功")
            select()
        }
    }

    /** 查询 */
    let tableLoading = ref<boolean>(false)
    let reset = ()=>{
        selectRolename.value = ""
        select()
    }
    // 初始化查询一次
    onMounted(()=>{
        select()
    })


    // 响应参数
    let pageInfo = ref<PageInfo<any>>()

    let select = async()=>{
        tableLoading.value = true;
        let r:R<any> = await request.post("/role/selectRoleList", {
            rolename: selectRolename.value,
            pageNum: pageRequest.value.pageNum,
            pageSize: pageRequest.value.pageSize
        })
        tableLoading.value = false;
        if(r.code == "success"){
            pageInfo.value = r.data
        }
    }
    let handleSizeChange = ()=>{
        select()
    } 

    /** 删除 */
    let deleteRole = (id:string)=>{
        ElMessageBox.confirm(
            '是否确认删除?',
            '是否确认删除?',
            {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
            }
        ).then(async() => {
            let r:R<any> = await request.post("/role/deleteRole", {
                id: id
            })
            if(r.code == "success"){
                ElMessage.success("删除成功")
                select();
            }
            
        }).catch(() => {
            
        })
        
    }

    /** 编辑 */


    let updateBuser = (row:any)=>{
        updateId.value = row.id
        updateRolename.value = row.rolename
        updatePermissionList.value = [...row.permissionList]

        // console.log("updatePermissionList.value", [...updatePermissionList.value])

        updateDialogVisible.value = true
    }
    let updateRoleCancel = ()=>{
        updateDialogVisible.value = false
    }

    let updateRoleSave = async ()=>{
        let r:R<any> = await request.post("/role/updateRole", {
            id: updateId.value,
            rolename: updateRolename.value,
            permissionList: updatePermissionList.value
        })
        if(r.code === "success"){
            updateDialogVisible.value = false
            ElMessage.success("更新角色成功")
            select()
        }
    }

    

    return {
        addRole,
        addDialogVisible,
        rolename,
        permissionList,
        addRoleCancel,
        addRoleSave,
        selectRolename,
        reset,
        select,
        tableLoading,
        pageInfo,
        updateBuser,
        deleteRole,
        pageRequest,
        updateDialogVisible,
        updateRolename,
        updatePermissionList,
        updateRoleCancel,
        updateRoleSave,
    }
}