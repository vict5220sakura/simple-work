package com.vict.framework.utils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.vict.framework.FrameworkCommon;
import com.vict.utils.PatternUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

@Slf4j
public class QiNiuUtils {

    /** fileName 文本前不允许有  /  */
    public static String uploadQiNiu(InputStream inputStream, String fileName){
        // File file = new File(filePath);
        //String fileName = file.getName();

        String accessKey = FrameworkCommon.qiNiuAk;
        String secretKey = FrameworkCommon.qiNiuSk;
        String bucket = FrameworkCommon.qiNiuBucket;
        Auth auth = Auth.create(accessKey, secretKey);


        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.huabei());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        //如果是Windows情况下，
        // String localFilePath = filePath;
        //默认不指定key的情况下，以文件内容的hash值作为文件名

        try {
            String upToken = auth.uploadToken(bucket, fileName, 3600, new StringMap().put("insertOnly", 0));

            // uploadManager.put()

            Response response = uploadManager.put(inputStream, fileName, upToken, null, null);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            log.info("上传成功, {}", fileName);
        } catch (QiniuException ex) {
            ex.printStackTrace();
            if (ex.response != null) {
                System.err.println(ex.response);
                try {
                    String body = ex.response.toString();
                    System.err.println(body);
                } catch (Exception ignored) {
                    log.error("", ignored);
                }
            }
        }
        return FrameworkCommon.qiNiuFileUrl + fileName;
    }

    public static String getAttname(String url){
        String s = PatternUtil.matcherOne("(?<=attname=).*(?=&|$)", url);
        return s;
    }
}
