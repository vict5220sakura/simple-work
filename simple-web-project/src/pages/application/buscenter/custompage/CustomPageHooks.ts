import { permissionList } from "@/config/PermissionConfig"
import { storeToRefs } from "pinia"
import { useCustomPageStore } from "./CustomPageStore"
import {request} from '@/utils/Request'
import type { CustomPageVO } from "./CustomPageTypes"
import {type R} from "@/types/R"
import { computed, onMounted, ref } from "vue"
import type { PageInfo } from "@/types/PageInfo"
import { ElMessage, ElMessageBox } from "element-plus"
import useClipboard from "vue-clipboard3";


let customPageStore = useCustomPageStore()
let {
    addCustomPageDialogVisible,
    addCustomName,
    addPageValue,
    selectCustomName,
    pageRequest,
    editDialogVisible,
    editorDialog,
    editorTarget,
    editorValue,
    updateId,
    updateCustomName,
    updatePageValue
} = storeToRefs(customPageStore)

const { toClipboard } = useClipboard();

// 富文本编辑对象
export enum CustomPageEditorTarget {
    addValue,
    editValue,
}

export const useCustomPageHooks = ()=>{

    const addCustomPage = ()=>{
        addCustomPageDialogVisible.value = true
    }

    const addCustomPageCancel = () =>{
        addCustomPageDialogVisible.value = false
    }

    const addCustomPageSave = async ()=>{
        let r:R<CustomPageVO> = await request.post("/customPage/insertCustomPage", {
            customName: addCustomName.value,
            pageValue: addPageValue.value
        })

        if(r.code === "success"){
            addCustomPageDialogVisible.value = false
            select()
            ElMessage.success("新增自定义页面成功")
        }
    }

    const reset = ()=>{
        selectCustomName.value = undefined
        select()
    }

    let pageInfo = ref<PageInfo<CustomPageVO>>()
    let tableLoading = ref<boolean>(false)
    const select = async ()=>{
        tableLoading.value = true;
        let r:R<PageInfo<CustomPageVO>> = await request.post("/customPage/customPageList", {
            customName: selectCustomName.value,
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

    // 富文本编辑器
    const editorSave = ()=>{
        if(editorTarget.value == CustomPageEditorTarget.addValue){
            addPageValue.value = editorValue.value
        }else if(editorTarget.value == CustomPageEditorTarget.editValue){
            updatePageValue.value = editorValue.value
        }
        editorDialog.value = false
        editorValue.value = undefined
    }
    const editorCancel = ()=>{
        editorDialog.value = false
    }

    const updateCustomPage = (row:CustomPageVO)=>{
        updateId.value = row.id
        updateCustomName.value = row.customName
        updatePageValue.value = row.pageValue

        editDialogVisible.value = true
    }

    const updateCustomPageCancel = ()=>{
        editDialogVisible.value = false
    }

    const updateCustomPageSave = async()=>{
        let r:R<any> = await request.post("/customPage/updateCustomPage", {
            id: updateId.value,
            customName: updateCustomName.value,
            pageValue: updatePageValue.value
        })
        if(r.code === "success"){
            editDialogVisible.value = false
            ElMessage.success("更新自定义页面成功")
            select()
        }
    }

    const deleteCustomPage = async(id:string)=>{
        ElMessageBox.confirm(
            '是否确认删除?',
            '是否确认删除?',
            {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
            }
        ).then(async() => {
            let r:R<any> = await request.post("/customPage/deleteCustomPage", {
                id: id,
            })
            if(r.code === "success"){
                ElMessage.success("删除自定义页面成功")
                select()
            }
            
        }).catch(() => {
            
        })
    }

    // 一个计算属性 ref
    const pageUrl = (id:string)=>{
        let url = String(window.location)
        return url.replace("/application/customPage", "/customPageShow?id=" + id)
    }

    const copyUrl = async (text:string)=>{
        await toClipboard(text);  //实现复制
        ElMessage.success("复制成功")
    }

    const addEditorShow = ()=>{
        editorDialog.value = true;
        editorTarget.value = CustomPageEditorTarget.addValue; 
        setTimeout(()=>{
            editorValue.value = addPageValue.value
        },0)
    }

    const updateEditorShow = ()=>{
        editorDialog.value = true;
        editorTarget.value = CustomPageEditorTarget.editValue; 
        setTimeout(()=>{
            editorValue.value = updatePageValue.value
        },0)
    }

    return {
        CustomPageEditorTarget,
        addCustomPage,
        permissionList,
        addCustomPageDialogVisible,
        addCustomName,
        addPageValue,
        addCustomPageCancel,
        addCustomPageSave,
        selectCustomName,
        reset,
        select,
        tableLoading,
        pageRequest,
        pageInfo,
        editDialogVisible,
        editorDialog,
        editorTarget,
        editorValue,
        editorCancel,
        editorSave,
        updateCustomPage,
        updateCustomName,
        updatePageValue,
        updateCustomPageCancel,
        updateCustomPageSave,
        deleteCustomPage,
        pageUrl,
        copyUrl,
        addEditorShow,
        updateEditorShow
    }
}