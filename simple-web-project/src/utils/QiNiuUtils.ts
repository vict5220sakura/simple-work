import {type QiniuConfig, type QiniuData, type QiniuUrl} from "@/types/QiNiuType"
import {request} from '@/utils/Request'
import {type R} from "@/types/R"
import {getNow} from "@/utils/TimeUtil"
import {randomId} from "@/utils/IdUtils"
import axios from "axios"

/** 获取七牛配置 */
export function getQiNiuConfig():Promise<R<any>>{
    return request.post("/util/qiniuToken")
}

/** 获取七牛token */
export async function getQiNiuToken():Promise<string>{
    let r:R<any> = await request.post("/util/qiniuToken")
    return r.data.token
}

export async function UploadFilled(file: File):Promise<QiniuUrl>{
    let r:R<any> = await getQiNiuConfig();
    let qiniuConfig:QiniuConfig = r.data

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
    let key = `${id}__${nowTime}${fileExtension}`;
    
    // 上传
    let param = new FormData()
    param.append('key', key)
    param.append('token', token)
    param.append('file', file, key)

    let config = {
        headers: {'Content-Type': 'multipart/form-data'}
    }

    await axios.post(qiniuConfig.qiNiuUploadHost as string, param, config)


    // 上传成功
    let ename = encodeURIComponent(file.name)
    let uploadPicUrl = `${qiniuConfig!.qiNiuFileUrl}${key}?attname=${ename}`;
    console.log("上传成功", uploadPicUrl)

    let res:QiniuUrl = {
        url: uploadPicUrl,
        name: file.name
    }
    return res
}