import {permissionList}  from '@/config/PermissionConfig'
import InnerDialog from '@/components/utils/InnerDialog.vue'
import { ref, onMounted, useTemplateRef } from 'vue';
import {useKeyValueStore, EditorTarget} from "@/pages/application/system/keyvalue/KeyValueStore"
import {storeToRefs} from 'pinia'
import {type R} from "@/types/R"
import {request} from "@/utils/Request"
import {ElMessage, ElMessageBox} from "element-plus"
import {type PageInfo} from "@/types/PageInfo"
import { useImageStore } from './ImageStore';
import {ImageSizeInstant, ImageType, type Image, type ImageSize} from "./ImageTypes"
import useClipboard from "vue-clipboard3";
import { FlipList } from '@/utils/FlipUtil';

// 缓存数据
let imageStore = useImageStore()
let {
    selectAttname,
    selectImageType,
    pageRequest,
    imageSize
} = storeToRefs(imageStore)

const { toClipboard } = useClipboard();

export const useImagesHooks = function(){
    const uploadImage = async (url:string)=>{
        let r:R<any> = await request.post("/image/insertImage", {
            url: url
        })
        if(r.code == "success"){
            ElMessage.success("上传成功")
            select()
        }
    }
    
    /** 查询 */
    let tableLoading = ref<boolean>(false)
    const reset = ()=> {
        selectAttname.value = undefined
        selectImageType.value = undefined
        select()
    }
    const select = async ()=>{
        checkAllFlag.value = false
        tableLoading.value = true;
        batchDeleteFlag.value = false;
        batchMoveFlag.value = false;

        try{
            pageInfo.value!.list = []
        }catch(err){console.error(err)}

        let r:R<PageInfo<Image>> = await request.post("/image/select", {
            attname: selectAttname.value,
            imageType: selectImageType.value,
            pageNum: pageRequest.value.pageNum,
            pageSize: pageRequest.value.pageSize
        })
        tableLoading.value = false;
        if(r.code === "success"){
            pageInfo.value = r.data
        }
    }
    // 响应参数
    let pageInfo = ref<PageInfo<Image>>()

    // 初始化查询一次
    onMounted(()=>{
        select()
    })

    const getImageSize = ():ImageSize=>{
        switch (imageSize.value) {
            case "small":
                return ImageSizeInstant.small
            case "middle":
                return ImageSizeInstant.middle
            case "large":
                return ImageSizeInstant.large
            default:
                return ImageSizeInstant.small
        }
    }

    const deleteImage = (id:string)=>{
        ElMessageBox.confirm(
            '是否确认删除?',
            '是否确认删除?',
            {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
            }
        ).then(async() => {
            let r:R<any> = await request.post("/image/deleteImage", {
                id: id,
            })
            if(r.code == "success"){
                ElMessage.success("删除成功")
                select()
            }
            
        })
    }

    const copyImage = async(data:string)=>{
        await toClipboard(data);  //实现复制
        ElMessage.success("复制成功")
    }

    let updateAttnameDialogVisible = ref<boolean>(false)
    let updateAttnameId = ref<string>()
    let updateAttname = ref<string>()

    const updateAttnameCancel = ()=>{
        updateAttnameDialogVisible.value = false
    }

    const willUpdateAttname = (item:Image)=>{
        updateAttnameId.value = item.id
        updateAttnameDialogVisible.value = true
        updateAttname.value = item.attname
    }

    const updateAttnameSave = async()=>{
        let r:R<any> = await request.post("/image/updateImageAttname", {
            id: updateAttnameId.value,
            attname: updateAttname.value
        })
        if(r.code == "success"){
            ElMessage.success("修改成功")
            updateAttnameDialogVisible.value = false
            select()
        }
    }

    const moveUp = async(id:string)=>{
        let r:R<any> = await request.post("/image/moveImage", {
            id: id,
            direction: "up"
        })
        if(r.code == "success"){
            select()
        }
    }

    const moveDown = async(id:string)=>{
        let r:R<any> = await request.post("/image/moveImage", {
            id: id,
            direction: "down"
        })
        if(r.code == "success"){
            select()
        }
    }

    const change = async(index1:number, index2:number)=>{
        let id1 = pageInfo.value?.list[index1]?.id
        let id2 = pageInfo.value?.list[index2]?.id
        let r:R<any> = await request.post("/image/changeImage", {
            id1: id1,
            id2: id2,
        })
        if(r.code == "success"){
            select()
        }
    }

    const checkAll = (b:boolean)=>{
        pageInfo.value?.list?.forEach((item)=>{
            item.isCheck = b
        })
    }

    const batchDelete = ()=>{
        let checkIdList:string[] = [];
        pageInfo.value?.list?.forEach((item)=>{
            if(item.isCheck == true){
                checkIdList.push(item.id)
            }
        })

        if(checkIdList.length == 0){
            ElMessage.warning("请选择要删除的图片")
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
            let r:R<any> = await request.post("/image/deleteImageMany", {
                ids: checkIdList
            })
            if(r.code == "success"){
                ElMessage.success("删除成功")
                select()
            }
        })
    }

    let batchDeleteFlag = ref<boolean>(false)
    const willBatchDelete = ()=>{
        batchDeleteFlag.value = true
    }
    const batchDeleteCancel = ()=>{
        batchDeleteFlag.value = false
        pageInfo.value?.list?.forEach((item)=>{
            item.isCheck = false
        })
    }

    let checkAllFlag = ref<boolean>(false)

    // svg上传
    let uploadSvgDialogVisible = ref<boolean>(false);
    const willUploadSvg = ()=>{
        uploadSvgDialogVisible.value = true
    }
    const uploadSvgCancel = ()=>{
        uploadSvgDialogVisible.value = false
    }
    let uploadSvgCode = ref<string>()
    const uploadSvgSave = async()=>{
        let r:R<any> = await request.post("/image/insertImage", {
            svgCode: uploadSvgCode.value
        })
        if(r.code == "success"){
            ElMessage.success("上传成功")
            select()
        }
        uploadSvgDialogVisible.value = false
    }

    // base64上传
    let uploadBase64DialogVisible = ref<boolean>(false);
    const willUploadBase64 = ()=>{
        uploadBase64DialogVisible.value = true
    }
    const uploadBase64Cancel = ()=>{
        uploadBase64DialogVisible.value = false
    }
    let uploadBase64Code = ref<string>()
    const uploadBase64Save = async()=>{
        let r:R<any> = await request.post("/image/insertImage", {
            base64Code: uploadBase64Code.value
        })
        if(r.code == "success"){
            ElMessage.success("上传成功")
            select()
        }
        uploadBase64DialogVisible.value = false
    }

    const getImageTagType = (imageType: ImageType)=>{
        if(imageType == ImageType.url){
            return "primary";
        }else if(imageType == ImageType.svg){
            return "warning";
        }else if(imageType == ImageType.base64){
            return "danger";
        }
    }

    const deleteCheck = (item:Image)=>{
        item.isCheck = !item.isCheck
    }

    // 移动
    let batchMoveFlag = ref<boolean>(false)
    const willBatchMove = ()=>{
        batchMoveFlag.value = true
        batchDeleteCancel()
    }
    const batchMoveCancel = ()=>{
        batchMoveFlag.value = false
        select()
    }
    const dragRef = useTemplateRef<any>("dragRef")
    const batchMoveSave = async ()=>{
        let children = [...dragRef.value.children]
        let ids:string[] = []
        children.forEach(item=>{
            ids.push(item.getAttribute("imageId"))
        })
        let r:R<any> = await request.post("/image/batchMove", {
            ids: ids
        })
        if(r.code == "success"){
            ElMessage.success("移动成功")
            batchMoveFlag.value = false
            select()
        }
    }
    /** 获取顶层div */
    const findDragElement = (target: any) => {
        try{
            if(target.getAttribute("draggable")=='true'){
                return target;
            }else{
                return findDragElement(target.parentElement)
            }
        }catch(err){
            return undefined
        }
    }
    let sourceElement: any = null
    let targetElement: any = null
    onMounted(() => {
        dragRef.value!.ondragstart = (e: any) => {
            let targetElement = findDragElement(e.target)

            setTimeout(() => {
                targetElement.children[0].classList.add("moving")
                // e.target.classList.add("moving")
            }, 0)
            sourceElement = targetElement
        }
        dragRef.value!.ondragover = (e: any) => {
            // e.preventDefault()
        }
        dragRef.value!.ondragenter = (e: any) => {
            // e.preventDefault()
            let findTargetElement = findDragElement(e.target)
            if (targetElement == findTargetElement) {
                return
            }
            targetElement = findTargetElement
            if (targetElement === sourceElement) {
                return
            }

            let children = [...dragRef.value.children]

            let sourceIndex = children.indexOf(sourceElement)
            let targetIndex = children.indexOf(targetElement)


            if (sourceIndex > targetIndex) {

                let sourceTag = pageInfo.value?.list[sourceIndex]
                if (!sourceTag || (targetIndex == null || targetIndex == undefined || targetIndex < 0)) {
                    return;
                }

                children.splice(sourceIndex, 1)
                // 动画{
                let list: any[] = []
                children.forEach(d => {
                    list.push(d.children[0])
                })
                let flipList = new FlipList(list, 0.5)
                // 动画}
                dragRef.value.insertBefore(sourceElement, targetElement)

                // 动画{
                flipList!.invert()
                // 动画}

            } else {
                let sourceTag = pageInfo.value?.list[sourceIndex]
                if (!sourceTag || !targetIndex) {
                    return;
                }

                // 动画{
                children.splice(sourceIndex, 1)
                let list: any[] = []
                children.forEach(d => {
                    list.push(d.children[0])
                })
                let flipList = new FlipList(list, 0.5)
                dragRef.value.insertBefore(sourceElement, targetElement.nextElementSibling)
                // 动画}

                // 动画{
                flipList!.invert()
                // 动画}
            }
        }

        dragRef.value!.ondragend = (e: any) => {
            let targetElement = findDragElement(e.target)
            targetElement.children[0].classList.remove("moving")
            // let children = [...dragRef.value.children]
            // children.forEach((element: any) => {
            //   element.children[0].classList.remove("pointer-events-none")
            // })
        }
    })
    return {
        permissionList,

        uploadImage,
        uploadSvgDialogVisible,
        uploadBase64DialogVisible,
        willUploadSvg,
        willUploadBase64,
        uploadSvgCancel,
        uploadBase64Cancel,
        uploadSvgCode,
        uploadBase64Code,
        uploadSvgSave,
        uploadBase64Save,

        selectAttname,
        selectImageType,

        pageRequest,
        select,
        pageInfo,
        reset,
        tableLoading,
        imageSize,

        getImageSize,

        deleteImage,
        batchDelete,
        batchDeleteFlag,
        willBatchDelete,
        batchDeleteCancel,

        copyImage,

        willUpdateAttname,
        updateAttnameDialogVisible,
        updateAttname,
        updateAttnameCancel,
        updateAttnameSave,

        moveDown,
        moveUp,
        change,

        checkAll,
        checkAllFlag,
        deleteCheck,

        getImageTagType,

        batchMoveFlag,
        batchMoveSave,
        willBatchMove,
        batchMoveCancel,
    }
}