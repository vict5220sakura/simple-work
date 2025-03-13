export interface QiniuConfig{
    qiNiuUploadHost?:string 
    qiNiuBucket?:string 
    qiNiuFileUrl?:string 
}

export interface QiniuData{
    key?:string, //图片名字处理
    token?:string //七牛云token
}

export interface QiniuUrl{
    url:string,
    name:string
}