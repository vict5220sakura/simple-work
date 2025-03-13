
import {permissionList} from "@/config/PermissionConfig";
import {useJobStore} from "@/pages/application/system/job/JobStore"
import {storeToRefs} from "pinia"
import {request} from "@/utils/Request"
import type {R} from "@/types/R";
import { ElMessage, ElMessageBox } from "element-plus";
import {ref, onMounted, useTemplateRef} from 'vue'
import type {PageInfo} from "@/types/PageInfo";
import {randomId} from '@/utils/IdUtils'
import {useChinesePoetryStore} from "./ChinesePoetryStore"
import type { ChinesePoetry, ChinesePoetryClassify } from "./ChinesePoetryTypes";
import { useQiniuUploadServiceHooks } from "@/components/utils/QiniuUploadServiceHooks";


let chinesePoetryStore = useChinesePoetryStore()
let {
    selectChinesePoetryClassifyId,
    addChinesePoetryClassifyDialogVisible,
    addChinesePoetryClassifyName,
    pageRequest,
    selectTitle,
    selectAuthor,

    addChinesePoetryDialogVisible,
    addChinesePoetryTitle,
    addChinesePoetryAuthor,
    addChinesePoetryParagraphs,

    updateChinesePoetryDialogVisible,
    updateChinesePoetryId,
    updateChinesePoetryTitle,
    updateChinesePoetryAuthor,
    updateChinesePoetryParagraphs,
} = storeToRefs(chinesePoetryStore)

let {
    urlGetFileName
} = useQiniuUploadServiceHooks()

export const useChinesePoetryHooks = function(){

    let chinesePoetryClassifyListLoading = ref<boolean>(false)

    let tableRef = useTemplateRef<any>("tableRef")

    const selectAllChinesePoetryClassify = async()=>{
        chinesePoetryClassifyListLoading.value = true
        let r:R<ChinesePoetryClassify[]> = await request.post("/chinesePoetryClassify/allChinesePoetryClassify")
        chinesePoetryClassifyList.value = r?.data
        chinesePoetryClassifyListLoading.value = false
    }

    onMounted(async()=>{
        await selectAllChinesePoetryClassify()
        if(selectChinesePoetryClassifyId.value != null && selectChinesePoetryClassifyId.value != undefined){
            chinesePoetryClassifyList.value.forEach((o:ChinesePoetryClassify)=>{
                if(o.id == selectChinesePoetryClassifyId?.value){
                    selectChinesePoetryClassify(o);
                    return;
                }
            })
            
        }
    })

    const getChinesePoetryClassifyById = (id:string):ChinesePoetryClassify|undefined=>{
        for(let item of chinesePoetryClassifyList.value){
            if(item.id == id){
                return item;
            }
        }
    }

    let chinesePoetryClassifyList = ref<ChinesePoetryClassify[]>([]);

    const addChinesePoetryClassify = ()=>{
        addChinesePoetryClassifyDialogVisible.value = true;
    }

    const addChinesePoetryClassifyCancel = ()=>{
        addChinesePoetryClassifyDialogVisible.value = false;
    }
    const addChinesePoetryClassifySave = async()=>{
        let r:R<any> = await request.post("/chinesePoetryClassify/insertChinesePoetryClassify", {
            classifyName: addChinesePoetryClassifyName.value
        })
        if(r?.code== "success"){
            addChinesePoetryClassifyDialogVisible.value = false;
            ElMessage.success("新增成功")
            selectAllChinesePoetryClassify()
        }
    }

    const selectChinesePoetryClassify = (item:ChinesePoetryClassify)=>{
        selectChinesePoetryClassifyId.value = item?.id
        select()
    }

    let deleteChinesePoetryClassifyFlag = ref<boolean>();

    const deleteChinesePoetryClassify = (id:string)=>{
        ElMessageBox.confirm(
            '此操作将永久删除该类目, 古诗将移动到其他类目, 是否继续?',
            '提示',
            {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
            }
        ).then(async()=>{
            let r:R<any> = await request.post("/chinesePoetryClassify/deleteChinesePoetryClassify", {
                id: id
            })
            if(r?.code== "success"){
                
                if(id == selectChinesePoetryClassifyId.value){
                    selectChinesePoetryClassifyId.value = undefined
                }
                ElMessage.success("删除成功")
                selectAllChinesePoetryClassify()
            }
        })
    }

    // 修改类目
    let updateChinesePoetryClassifyFlag = ref<boolean>(false)
    const willUpdateChinesePoetryClassify = (id:string)=>{
        updateChinesePoetryClassifyId.value = id
        let item:ChinesePoetryClassify|undefined = getChinesePoetryClassifyById(id)
        updateChinesePoetryClassifyName.value = item?.classifyName

        updateChinesePoetryClassifyDialogVisible.value = true;
    }
    let updateChinesePoetryClassifyDialogVisible = ref<boolean>(false);
    let updateChinesePoetryClassifyName = ref<string>()
    let updateChinesePoetryClassifyId = ref<string>()
    const updateChinesePoetryClassifyCancel = ()=>{
        updateChinesePoetryClassifyDialogVisible.value = false;
    }
    const updateChinesePoetryClassifySave = async()=>{
        let r:R<any> = await request.post("/chinesePoetryClassify/updateChinesePoetryClassify", {
            id: updateChinesePoetryClassifyId.value,
            classifyName: updateChinesePoetryClassifyName.value
        })
        if(r?.code== "success"){
            ElMessage.success("修改成功")
            updateChinesePoetryClassifyDialogVisible.value = false;
            selectAllChinesePoetryClassify()
        }
    }

    // 移动类目
    let moveChinesePoetryClassifyFlag = ref<boolean>(false)
    const classifyMoveUp = async(id:string)=>{
        let r:R<PageInfo<ChinesePoetry>> = await request.post("/chinesePoetryClassify/move", {
            id: id,
            direction: "up"
        })
        if(r.code == "success"){
            selectAllChinesePoetryClassify()
        }
    }
    const classifyMoveDown = async(id:string)=>{
        let r:R<PageInfo<ChinesePoetry>> = await request.post("/chinesePoetryClassify/move", {
            id: id,
            direction: "down"
        })
        if(r.code == "success"){
            selectAllChinesePoetryClassify()
        }
    }

    // 列表
    let tableLoading = ref<boolean>(false)

    let pageInfo = ref<PageInfo<ChinesePoetry>>()

    const select = async ()=>{
        tableLoading.value = true

        let r:R<PageInfo<ChinesePoetry>> = await request.post("/chinesePoetry/selectChinesePoetry", {
            title: selectTitle.value,
            author: selectAuthor.value,
            classifyId: selectChinesePoetryClassifyId.value,
            pageNum: pageRequest.value.pageNum,
            pageSize: pageRequest.value.pageSize
        })
        
        if(r.code == "success"){
            pageInfo.value = r.data
        }

        tableLoading.value = false
    }

    const reset = ()=>{
        selectTitle.value = undefined
        selectAuthor.value = undefined
        select()
    }

    const moveUp = async(id:string)=>{
        let r:R<any> = await request.post("/chinesePoetry/move", {
            id: id,
            direction: "up"
        })
        if(r.code == "success"){
            select()
        }
    }
    const moveDown = async(id:string)=>{
        let r:R<any> = await request.post("/chinesePoetry/move", {
            id: id,
            direction: "down"
        })
        if(r.code == "success"){
            select()
        }
    }


    // 新增古诗
    const willAddChinesePoetry = ()=>{
        addChinesePoetryDialogVisible.value = true
    }
    const addChinesePoetryCancel = ()=>{
        addChinesePoetryDialogVisible.value = false
    }
    const addChinesePoetrySave = async ()=>{
        let r:R<any> = await request.post("/chinesePoetry/insertChinesePoetry", {
            title: addChinesePoetryTitle.value,
            author: addChinesePoetryAuthor.value,
            paragraphs: addChinesePoetryParagraphs.value,
            classifyId: selectChinesePoetryClassifyId.value
        })
        if(r?.code== "success"){
            addChinesePoetryDialogVisible.value = false
            ElMessage.success("新增成功")
            select()
        }
    }
    const cleanAddChinesePoetry = ()=>{
        addChinesePoetryTitle.value = undefined
        addChinesePoetryAuthor.value = undefined
        addChinesePoetryParagraphs.value = undefined
    }

    // 删除古诗
    const willDeleteChinesePoetry = (id:string)=>{
        ElMessageBox.confirm(
            '此操作将永久删除该古诗, 是否继续?',
            '提示',
            {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
            }
        ).then(async()=>{
            let r:R<any> = await request.post("/chinesePoetry/deleteChinesePoetry", {
                id: id
            })
            if(r?.code== "success"){
                ElMessage.success("删除成功")
                select()
            }
        })
    }

    const willDeleteMany = ()=>{
        let list:ChinesePoetry[] = tableRef.value.getSelectionRows()
        if(list.length == 0){
            ElMessage.warning("请选择要删除的古诗")
            return;
        }

        let chinesePoetryIds:string[] = []
        list.forEach(item=>{
            chinesePoetryIds.push(item.id)
        })

        ElMessageBox.confirm(
            '此操作将永久删除这些古诗, 是否继续?',
            '提示',
            {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
            }
        ).then(async()=>{
            let r:R<any> = await request.post("/chinesePoetry/deleteChinesePoetryMany", {
                chinesePoetryIds: chinesePoetryIds
            })
            if(r?.code== "success"){
                ElMessage.success("删除成功")
                select()
            }
        })
    }

    // 更新
    const willUpdateChinesePoetry = (item:ChinesePoetry)=>{
        updateChinesePoetryId.value = item.id
        updateChinesePoetryTitle.value = item.title
        updateChinesePoetryAuthor.value = item.author
        updateChinesePoetryParagraphs.value = item.paragraphs

        updateChinesePoetryDialogVisible.value = true
    }

    const updateChinesePoetryCancel = ()=>{
        updateChinesePoetryDialogVisible.value = false
    }

    const updateChinesePoetrySave = async()=>{
        let r:R<any> = await request.post("/chinesePoetry/updateChinesePoetry", {
            id: updateChinesePoetryId.value,
            title: updateChinesePoetryTitle.value,
            author: updateChinesePoetryAuthor.value,
            paragraphs: updateChinesePoetryParagraphs.value,
        })
        if(r.code === "success"){
            updateChinesePoetryDialogVisible.value = false
            ElMessage.success("更新古诗成功")
            select()
        }
    }

    // 修改类目
    const willChangeChinesePoetryClassify = ()=>{
        let list = tableRef.value.getSelectionRows()
        if(list.length == 0){
            ElMessage.warning("请选择要修改的古诗")
        }
        // changeChinesePoetryClassifyId.value = selectChinesePoetryClassifyId.value
        changeChinesePoetryClassifyDialogVisible.value = true
    }
    let changeChinesePoetryClassifyDialogVisible = ref<boolean>(false)
    let changeChinesePoetryClassifyId = ref<string>()

    const changeChinesePoetryClassifyCancel = ()=>{
        changeChinesePoetryClassifyDialogVisible.value = false
    }

    const changeChinesePoetryClassifySave = async()=>{
        let list:ChinesePoetry[] = tableRef.value.getSelectionRows()
        let chinesePoetryIds:string[] = []
        list.forEach(item=>{
            chinesePoetryIds.push(item.id)
        })
        let r:R<any> = await request.post("/chinesePoetry/changeChinesePoetryClassify", {
            chinesePoetryClassifyId: changeChinesePoetryClassifyId.value,
            chinesePoetryIds: chinesePoetryIds
        })
        changeChinesePoetryClassifyDialogVisible.value = false
        select()
    }

    // 导入数据
    const willImportData = ()=>{
        importDataFileUrl.value = undefined
        importDataDialogVisible.value = true
    }
    let importDataDialogVisible = ref<boolean>()
    let importDataFileUrl = ref()

    const importDataCancel = ()=>{
        importDataFileUrl.value = undefined
        importDataDialogVisible.value = false
    }
    const importDataSave = async()=>{
        let r:R<any> = await request.post("/chinesePoetry/importData", {
            fileUrl: importDataFileUrl.value,
            classifyId: selectChinesePoetryClassifyId.value,
            importData: importData.value
        })
        if(r.code == "success"){
            ElMessage.success("导入中...");
            importDataDialogVisible.value = false
            select()
        }
    }
    let importData = ref<string>();
    
    // 格式化古诗
    const formatParagraphs = (paragraphs:string, sort:boolean)=>{
        if(paragraphs == undefined || paragraphs == null){
            return undefined
        }
        let showParagraphs = paragraphs
        if(showParagraphs.length > 60 && sort){
            showParagraphs = showParagraphs.substring(0, 60) + "..."
        }
        let paragraphsFormat = showParagraphs.replaceAll("\n", "<br>")
        return paragraphsFormat;
    }

    return {
        permissionList,
        chinesePoetryClassifyList,
        addChinesePoetryClassifyDialogVisible,
        addChinesePoetryClassify,
        addChinesePoetryClassifyName,
        addChinesePoetryClassifyCancel,
        addChinesePoetryClassifySave,
        chinesePoetryClassifyListLoading,
        selectChinesePoetryClassify,
        deleteChinesePoetryClassifyFlag,
        deleteChinesePoetryClassify,
        selectChinesePoetryClassifyId,

        // 修改类目
        updateChinesePoetryClassifyFlag,
        willUpdateChinesePoetryClassify,
        updateChinesePoetryClassifyDialogVisible,
        updateChinesePoetryClassifyName,
        updateChinesePoetryClassifyCancel,
        updateChinesePoetryClassifySave,

        // 移动类目
        moveChinesePoetryClassifyFlag,
        classifyMoveUp,
        classifyMoveDown,

        tableLoading,
        pageInfo,
        pageRequest,
        select,
        reset,
        selectTitle,
        selectAuthor,
        moveUp,
        moveDown,

        willAddChinesePoetry,
        addChinesePoetryDialogVisible,
        addChinesePoetryTitle,
        addChinesePoetryAuthor,
        addChinesePoetryParagraphs,
        addChinesePoetryCancel,
        addChinesePoetrySave,
        cleanAddChinesePoetry,

        willDeleteChinesePoetry,
        willDeleteMany,

        willUpdateChinesePoetry,
        updateChinesePoetryDialogVisible,
        updateChinesePoetryId,
        updateChinesePoetryTitle,
        updateChinesePoetryAuthor,
        updateChinesePoetryParagraphs,
        updateChinesePoetryCancel,
        updateChinesePoetrySave,

        // 修改类目
        willChangeChinesePoetryClassify,
        changeChinesePoetryClassifyDialogVisible,
        changeChinesePoetryClassifyId,
        changeChinesePoetryClassifyCancel,
        changeChinesePoetryClassifySave,

        // 导入数据
        willImportData,
        importDataDialogVisible,
        importDataFileUrl,
        urlGetFileName,
        importDataCancel,
        importDataSave,
        importData,

        formatParagraphs,
    }
}