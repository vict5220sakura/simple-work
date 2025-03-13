import {request} from '@/utils/Request'
import {type R} from "@/types/R"
import { ref, useTemplateRef} from 'vue'
import { ElMessage } from 'element-plus'
import {getNow} from "@/utils/TimeUtil"
import {randomId} from "@/utils/IdUtils"
import {type QiniuConfig, type QiniuData} from "@/types/QiNiuType"
import {getQiNiuConfig, getQiNiuToken} from "@/utils/QiNiuUtils"
import {} from "@/utils/QiNiuUtils"

export default function(){

    // 七牛配置
    let qiniuConfig:QiniuConfig|null = null
    // 上传组件
    let uploadRef:any|null = null
    // 上传域名
    let domain = ref<string|null>()
    // 消息通知
    let emit:any = null
    // 初始化
    const init = (uploadRefDto:any, emitDto:any)=>{
        uploadRef = uploadRefDto
        emit = emitDto
    }

    // 获取七牛等配置
    getQiNiuConfig().then((r:R<any>)=>{
        qiniuConfig = r.data
        domain.value = qiniuConfig!.qiNiuUploadHost
    })
    
    // 七牛Data
    let qiniuData = ref<QiniuData>({})

    // 文本列表
    let fileList = ref([])

    // 移除方法
    const handleRemove = (file:any, fileList:any)=>{
        uploadPicUrl.value = null
    }

    // 上传后的图片地址
    let uploadPicUrl = ref<string|null>(null)

    // 上传异常
    const uploadError = (err:any, file:any, fileList:any)=>{
        ElMessage({
            message: "上传出错，请重试！",
            type: "error",
            center: true,
            duration: 1500
        });
    }

    // 多选错误
    const handleExceed = (files:any, fileList:any)=> {
        ElMessage({
            message: "当前限制选择 1 个文件",
            type: "warning",
            center: true,
            duration: 1500
        });
    }

    
    // 上传前获取token
    const beforeAvatarUpload = async (file:any)=>{
        let token:string = await getQiNiuToken()
        let nowTime = getNow()
        
        //这个this.fileExtension是文件名的后缀
        let lastDot = file.name.lastIndexOf(".")
        let fileName = file.name
        let fileExtension = ""
        if(lastDot >= 0){
            fileName = file.name.substring(0, lastDot)
            fileExtension = "." + file.name.substring(lastDot + 1)
        }

        let id = randomId(10)
        
        //这里的key给加上了时间戳，目的是为了防止上传冲突
        qiniuData.value.key = `${id}__${nowTime}${fileExtension}`;
        qiniuData.value.token = token
    }

    
    // 上传成功
    const uploadSuccess = (response:any, file:any, fileList:any)=>{
        let lastDot = file.name.lastIndexOf(".")
        let fileName = file.name
        let fileExtension = ""
        if(lastDot >= 0){
            fileName = file.name.substring(0, lastDot)
            fileExtension = "." + file.name.substring(lastDot + 1)
        }
        let ename = encodeURIComponent(file.name)
        let uploadPicUrl = `${qiniuConfig!.qiNiuFileUrl}${response.key}?attname=${ename}`;
        console.log("上传成功", uploadPicUrl)
        //在这里你就可以获取到上传到七牛的外链URL了
        emit("getUrl", uploadPicUrl)
        uploadRef.value.clearFiles()
    }

    // 移除方法
    const beforeRemove = (file:any, fileList:any)=> {
        // return this.$confirm(`确定移除 ${ file.name }？`);
    }

    return {
        qiniuData,
        handleRemove,
        uploadError,
        handleExceed,
        beforeAvatarUpload,
        domain,
        uploadSuccess,
        beforeRemove,
        fileList,
        init,
        
    }
}