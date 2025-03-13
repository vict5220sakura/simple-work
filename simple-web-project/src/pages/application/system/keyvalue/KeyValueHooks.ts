import {permissionList}  from '@/config/PermissionConfig'
import InnerDialog from '@/components/utils/InnerDialog.vue'
import { ref, onMounted } from 'vue';
import {useKeyValueStore, EditorTarget} from "@/pages/application/system/keyvalue/KeyValueStore"
import {storeToRefs} from 'pinia'
import {type R} from "@/types/R"
import {request} from "@/utils/Request"
import {ElMessage, ElMessageBox} from "element-plus"
import {type PageInfo} from "@/types/PageInfo"

export const useKeyValueHooks = function(){
    // 存储
    const keyValueStore = useKeyValueStore()
    let {
        addKey,
        addValue1,
        addValue2,
        addDesc,
        addDialogVisible,
        selectKey,
        selectDesc,
        pageRequest,
        updateDialogVisible,
        updateKey,
        updateValue1,
        updateValue2,
        updateDesc,
        updateId,
        editorDialog,
        editorTarget,
        editorValue
    } = storeToRefs(keyValueStore)

    // // 每次初始化都是第一页
    // pageRequest.value.pageNum = 1

    /** 新增 */

    let addKeyValue = ()=>{
        addDialogVisible.value = true
    }

    let addKeyValueCancel = ()=>{
        addDialogVisible.value = false
    }  

    let addKeyValueSave = async()=>{
        let r:R<any> = await request.post("/keyValue/insertKeyValue", {
            key: addKey.value,
            value1: addValue1.value,
            value2: addValue2.value,
            desc: addDesc.value
        })
        if(r.code === "success"){
            addDialogVisible.value = false
            ElMessage.success("新增系统参数成功")
            select()
        }
    }

    /** 查询 */
    let tableLoading = ref<boolean>(false)
    const reset = ()=> {
        selectKey.value = null
        selectDesc.value = null
        select()
    }

    const select = async ()=> {
        tableLoading.value = true;
        let r:R<any> = await request.post("/keyValue/keyValueList", {
            key: selectKey.value,
            desc: selectDesc.value,
            pageNum: pageRequest.value.pageNum,
            pageSize: pageRequest.value.pageSize
        })
        tableLoading.value = false;
        if(r.code == "success"){
            pageInfo.value = r.data
        }
    }

    // 初始化查询一次
    onMounted(()=>{
        select()
    })

    // 响应参数
    let pageInfo = ref<PageInfo<any>>()

    /** 删除 */
    const deleteKeyValue = async(id:string)=> {
        ElMessageBox.confirm(
            '是否确认删除?',
            '是否确认删除?',
            {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
            }
        ).then(async() => {
            let r:R<any> = await request.post("/keyValue/deleteKeyValueById", {
                id: id
            })
            if(r.code == "success"){
                ElMessage.success("删除成功")
                select();
            }
            
        }).catch(() => {
            
        })
    }

    /** 更新 */
    const updateKeyValue = async(row:any)=> {
        updateId.value = row.id
        updateKey.value = row.key
        updateValue1.value = row.value1
        updateValue2.value = row.value2
        updateDesc.value = row.desc
        
        updateDialogVisible.value = true
    }

    const updateKeyValueCancel = ()=>{
        updateDialogVisible.value = false
    }

    const updateKeyValueSave = async ()=>{
        let r:R<any> = await request.post("/keyValue/updateKeyValueById", {
            id: updateId.value,
            key: updateKey.value,
            value1: updateValue1.value,
            value2: updateValue2.value,
            desc: updateDesc.value
        })
        if(r.code === "success"){
            updateDialogVisible.value = false
            ElMessage.success("更新系统参数成功")
            select()
        }
    }

    // 富文本编辑器
    const editorSave = ()=>{
        if(editorTarget.value == EditorTarget.addValue1){
            addValue1.value = editorValue.value
        }else if(editorTarget.value == EditorTarget.addValue2){
            addValue2.value = editorValue.value
        }else if(editorTarget.value == EditorTarget.updateValue1){
            updateValue1.value = editorValue.value
        }else if(editorTarget.value == EditorTarget.updateValue2){
            updateValue2.value = editorValue.value
        }
        editorDialog.value = false
        editorValue.value = ""
    }
    const editorCancel = ()=>{
        editorDialog.value = false
    }

    const addEditor1Show = ()=>{
        editorDialog.value = true;
        editorTarget.value = EditorTarget.addValue1; 
        setTimeout(()=>{
            editorValue.value = addValue1.value
        },0)
    }

    const addEditor2Show = ()=>{
        editorDialog.value = true;
        editorTarget.value = EditorTarget.addValue2; 
        setTimeout(()=>{
            editorValue.value = addValue2.value
        },0)
    }

    const updateEditor1Show = ()=>{
        editorDialog.value = true;
        editorTarget.value = EditorTarget.updateValue1; 
        setTimeout(()=>{
            editorValue.value = updateValue1.value
        },0)
    }

    const updateEditor2Show = ()=>{
        editorDialog.value = true;
        editorTarget.value = EditorTarget.updateValue2; 
        setTimeout(()=>{
            editorValue.value = updateValue2.value
        },0)
    }

    return {
        addKeyValue,
        permissionList,
        addDialogVisible,
        addKey,
        addValue1,
        addValue2,
        addDesc,
        addKeyValueCancel,
        addKeyValueSave,
        selectKey,
        selectDesc,
        reset,
        select,
        tableLoading,
        pageInfo,
        updateKeyValue,
        deleteKeyValue,
        pageRequest,
        updateDialogVisible,
        updateKey,
        updateValue1,
        updateValue2,
        updateDesc,
        updateKeyValueCancel,
        updateKeyValueSave,
        editorDialog,
        editorTarget,
        editorValue,
        editorSave,
        editorCancel,
        EditorTarget,
        addEditor1Show,
        addEditor2Show,
        updateEditor1Show,
        updateEditor2Show
    }
}