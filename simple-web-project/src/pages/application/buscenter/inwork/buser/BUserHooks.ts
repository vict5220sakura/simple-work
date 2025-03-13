import { ref, onMounted } from 'vue';
import {emitter} from '@/utils/Emitter'
import {useBUserStore} from '@/pages/application/buscenter/inwork/buser/BUserStore'
import { storeToRefs } from 'pinia';
import {request} from '@/utils/Request'
import { ElMessage, ElMessageBox } from 'element-plus';
import {type R} from "@/types/R"
import {type PageInfo} from '@/types/PageInfo'
import {type PageRequest} from '@/types/PageRequest'
import {md5} from "js-md5";
import {Optional} from '@/utils/Optional'
import {useRoleHooks} from "@/pages/application/buscenter/inwork/role/RoleHooks"
import {permissionList}  from '@/config/PermissionConfig'
import {useRoleServiceHooks} from "@/pages/application/buscenter/inwork/role/RoleServiceHooks"

export default function(){
    /** 存储 */
    let bUserStore = useBUserStore()

    let {selectRoleList} = useRoleServiceHooks()

    let {
        addDialogVisible, 
        username, 
        password, 
        buserCode, 
        roleId,
        selectUsername, 
        selectBuserCode,
        pageRequest,
        updateDialogVisible,
        updateId,
        updateUsername,
        updatePassword,
        updateBuserCode,
        updateRoleId
        
    } = storeToRefs(bUserStore)

    // 每次初始化都是第一页
    // pageRequest.value.pageNum = 1

    /** 新增用户 */
    // 查询角色
    let roleAll = ref<any[]>([])
    onMounted(async()=>{
        let r:R<any> = await selectRoleList()
        roleAll.value = Optional.of(r).map((o:any)=> {return o.data}).map((o:any)=>{return o.list}).orElse([])
    })
    // 
    let addBUser = async ()=>{
        addDialogVisible.value = true
    }

    let addBUserCancel = ()=> {
        addDialogVisible.value = false
    }

    let addBUserSave = async()=>{
        
        let r:R<any> = await request.post("/bUser/addUser", {
            username: username.value,
            password: Optional.of(password.value).map((o:any)=>{return md5(o)}).orElse(null),
            buserCode: buserCode.value,
            roleId: roleId.value
        })
        if(r.code === "success"){
            addDialogVisible.value = false
            ElMessage.success("新增用户成功")
            select()
        }
    }

    /** 查询 */
    let tableLoading = ref<boolean>(false)
    let reset = ()=>{
        selectUsername.value = ""
        selectBuserCode.value = ""
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
        let r:R<any> = await request.post("/bUser/selectBuserList", {
            username: selectUsername.value,
            buserCode: selectBuserCode.value,
            pageNum: pageRequest.value.pageNum,
            pageSize: pageRequest.value.pageSize
        })
        tableLoading.value = false;
        if(r.code == "success"){
            pageInfo.value = r.data
            // bUserStore.setBUserList(r.data)
        }
    }
    let handleSizeChange = ()=>{
        select()
    } 

    /** 删除 */
    let deleteBuser = (id:string)=>{
        ElMessageBox.confirm(
            '是否确认删除?',
            '是否确认删除?',
            {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
            }
        ).then(async() => {
            let r:R<any> = await request.post("/bUser/deleteBuser", {
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
        updateUsername.value = row.username
        updatePassword.value = row.password
        updateBuserCode.value = row.buserCode 
        updateRoleId.value = row.roleId
        updateDialogVisible.value = true
    }
    let updateBUserCancel = ()=>{
        updateDialogVisible.value = false
    }

    let updateBUserSave = async ()=>{
        let r:R<any> = await request.post("/bUser/updateBuser", {
            id: updateId.value,
            username: updateUsername.value,
            password: Optional.of(updatePassword.value).map((o:any)=>{return md5(o)}).orElse(null),
            buserCode: updateBuserCode.value,
            roleId: updateRoleId.value
        })
        if(r.code === "success"){
            updateDialogVisible.value = false
            ElMessage.success("更新用户成功")
            select()
        }
    }

    return {
        addBUser,
        permissionList,
        addDialogVisible,
        buserCode,
        username,
        password,
        roleId,
        roleAll,
        addBUserCancel,
        addBUserSave,
        selectUsername,
        selectBuserCode,
        reset,
        select,
        tableLoading,
        pageInfo,
        updateBuser,
        deleteBuser,
        pageRequest,
        updateDialogVisible,
        updateBuserCode,
        updateUsername,
        updatePassword,
        updateRoleId,
        updateBUserCancel,
        updateBUserSave
    }
}