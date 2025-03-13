import {type R} from "@/types/R"
import {request} from "@/utils/Request"
import {ElMessage, ElMessageBox } from "element-plus"
import {onMounted, ref, useTemplateRef } from "vue"
import {permissionList} from "@/config/PermissionConfig";
import {useOrganizeStore} from "@/pages/application/buscenter/inwork/organize/OrganizeStore"
import {storeToRefs} from 'pinia'
import type { CheckChooseVO, OrganizeVO } from "@/pages/application/buscenter/inwork/organize/OrganizeTypes";
import { Choosed, type CheckFunc } from "@/components/buser/ChooseBuserTypes";
import {randomId} from "@/utils/IdUtils";
import {type BuserVO} from "@/pages/application/buscenter/inwork/buser/BuserTypes";


let organizeStore = useOrganizeStore()
let {
    chooseOrganizeId,
    chooseOrganize
} = storeToRefs(organizeStore)


export const useOrganizehooks = function(){
    const chooseBuserRef = useTemplateRef<any>("chooseBuserRef")
    const buserRef = useTemplateRef<any>("buserRef")
    
    /** 查询所有组织 */
    onMounted(()=>{
        selectAll()
    })
    let allOrganizeData = ref<OrganizeVO[]>()
    let allOrganizeDataMap:Map<string, OrganizeVO> = new Map();
    let defaultProps = {
        label: 'organizeName',
        children: 'children'
    }
    const selectAll = async ()=>{
        let r:R<OrganizeVO[]> = await request.post("/organize/selectAllOrganize")
        if(r.code === "success"){
            allOrganizeData.value = r.data
            packageDataMap(allOrganizeData.value)
        }
    }
    const packageDataMap = (organizes:OrganizeVO[])=>{
        for(let organize of organizes){
            allOrganizeDataMap.set(organize.id, organize)
            if(organize.children){
                packageDataMap(organize.children)
            }
        }
    }
    // 过滤
    const filterNode = (value: string, data: OrganizeVO) => {
        if (!value) return true
        return data.organizeName.includes(value)
    }

    /** 当前选择组织 */
    const handleOrganizeClick = (data: OrganizeVO) => {
        chooseOrganize.value = data;
        chooseOrganizeId.value = chooseOrganize.value.id

        // 刷新列表
        setTimeout(()=>{
            buserRef.value.select()
        },0)
    }

    /** 修改名称 */
    let changeNameDialog = ref<boolean>()
    let changeNameName = ref<string>()
    const changeName = ()=>{
        if(!chooseOrganize.value){
            ElMessage.warning("请选择组织")
            return;
        }
        changeNameName.value = chooseOrganize.value?.organizeName
        changeNameDialog.value = true
    }
    const changeNameCancel = ()=>{
        changeNameDialog.value = false
    }
    const changeNameSave = async ()=>{
        let r:R<OrganizeVO> = await request.post("/organize/changeName", {
            id: chooseOrganize.value?.id,
            organizeName: changeNameName.value
        })
        if(r.code === "success"){
            ElMessage.success("修改成功")
            
            let changeNameOrganize:OrganizeVO = allOrganizeDataMap.get(chooseOrganize.value!.id)!
            changeNameOrganize.organizeName = changeNameName.value!

            changeNameDialog.value = false
        }
    }

    /** 添加组织 */
    let addOrganizeDialog = ref<boolean>()
    let addOrganizeName = ref<string>()
    const addOrganize = ()=>{
        if(!chooseOrganize.value){
            ElMessage.warning("请选择组织")
            return;
        }
        addOrganizeName.value = undefined
        addOrganizeDialog.value = true
    }
    const addOrganizeCancel = ()=>{
        addOrganizeDialog.value = false
    }
    const addOrganizeSave = async ()=>{
        let r:R<OrganizeVO> = await request.post("/organize/addOrganize", {
            fatherId: chooseOrganize.value?.id,
            organizeName: addOrganizeName.value
        })
        if(r.code === "success"){
            ElMessage.success("新增成功")
            
            let addOrganize:OrganizeVO = r.data
            
            if(!chooseOrganize.value?.children){
                chooseOrganize.value!.children = []
            }

            chooseOrganize.value?.children.push(addOrganize)
            allOrganizeDataMap.set(addOrganize.id, addOrganize)

            addOrganizeDialog.value = false
        }
    }

    /** 删除组织 */
    const deleteOrganize = async ()=>{
        if(!chooseOrganize.value){
            ElMessage.warning("请选择组织")
            return;
        }

        ElMessageBox.confirm(
            '是否确认删除?',
            '是否确认删除?',
            {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
            }
        ).then(async() => {
            let r:R<undefined> = await request.post("/organize/deleteOrganize", {
                id: chooseOrganize.value?.id,
            })
            if(r.code == "success"){
                ElMessage.success("删除成功")

                let fatherOrganize = allOrganizeDataMap.get(chooseOrganize.value!.fatherId)
                let deleteOrganize = allOrganizeDataMap.get(chooseOrganize.value!.id)

                let deleteOrganizeIndex = fatherOrganize?.children.indexOf(deleteOrganize!)
                fatherOrganize?.children.splice(deleteOrganizeIndex!, 1)
            }
            
        }).catch(() => {
            
        })
            
    }

    /** 用户 */
    // 选择组织id
    let buserChooseOrganizeId = ref<string>()
    let needAddUserDialog = ref<boolean>()
    const needAddUser = ()=>{
        if(!chooseOrganize.value){
            ElMessage.warning("请选择组织")
            return;
        }
        buserChooseOrganizeId.value = chooseOrganize.value.id

        chooseBuserRef?.value?.clearSelection()

        needAddUserDialog.value = true
    }
    // 校验是否可以选中
    const checkChoose:CheckFunc = async (ids:string[]):Promise<Map<string,Choosed>>=>{

        let r:R<CheckChooseVO> = await request.post("/organize/checkChoose", {
            organizeId: chooseOrganize.value?.id,
            ids: ids
        })
        let map = new Map<string,Choosed>()
        if(r.code === "success"){
            for(let id of ids){
                if(r.data.map[id] == Choosed.choosed){
                    map.set(id, Choosed.choosed)
                }else{
                    map.set(id, Choosed.not)
                }
            }
        }
        
        return map
    }
    const addUserCancel = ()=>{
        needAddUserDialog.value = false
    }
    const addUserSave = async()=>{
        let buserVOs:BuserVO[] = chooseBuserRef?.value?.getSelectionRows()
        let ids:string[] = []
        for(let buserVO of buserVOs){
            ids.push(buserVO.id)
        }

        let r:R<any> = await request.post("/organize/addOrganizeChoose", {
            organizeId: chooseOrganize.value?.id,
            ids: ids
        })
        if(r.code === "success"){
            ElMessage.success("添加成功")
        }
        // 刷新列表
        buserRef.value.select()
        needAddUserDialog.value = false
    }

    const removeBuser = async (buserId:string)=>{
        let r:R<any> = await request.post("/organize/removeOrganizeChoose", {
            organizeId: chooseOrganize.value?.id,
            buserId: buserId
        })
        if(r.code === "success"){
            ElMessage.success({
                message: '移除成功',
                type: 'success', // 类型：success, warning, info, error
                duration: 1000,  // 显示时间，默认 3000 毫秒，为 0 时需手动关闭
                showClose: false, // 显示关闭按钮
                grouping: true,  // 是否将同类型消息合并
            })
            // 刷新列表
            buserRef.value.select()
        }
    }

    const removeBuserBatch = async ()=>{
        let buserVOs:BuserVO[] = buserRef?.value?.getSelectionRows()

        let buserIds:string[] = []

        for(let buserVO of buserVOs){
            buserIds.push(buserVO.id)
        }

        let r:R<any> = await request.post("/organize/removeOrganizeChooseBatch", {
            organizeId: chooseOrganize.value?.id,
            buserIds: buserIds
        })
        if(r.code === "success"){
            ElMessage.success({
                message: '移除成功',
                type: 'success', // 类型：success, warning, info, error
                duration: 1000,  // 显示时间，默认 3000 毫秒，为 0 时需手动关闭
                showClose: false, // 显示关闭按钮
                grouping: true,  // 是否将同类型消息合并
            })
            // 刷新列表
            buserRef.value.select()
        }
    }

    return {
        permissionList,
        chooseOrganizeId,
        allOrganizeData,
        defaultProps,
        filterNode,
        handleOrganizeClick,
        changeName,
        changeNameDialog,
        changeNameName,
        changeNameCancel,
        changeNameSave,
        addOrganize,
        addOrganizeDialog,
        addOrganizeName,
        addOrganizeCancel,
        addOrganizeSave,
        deleteOrganize,
        chooseOrganize,
        needAddUser,
        needAddUserDialog,
        buserChooseOrganizeId,
        checkChoose,
        addUserCancel,
        addUserSave,
        removeBuser,
        removeBuserBatch
    }
}