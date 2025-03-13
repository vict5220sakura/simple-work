package com.vict.utils;

import cn.hutool.core.util.URLUtil;
import cn.hutool.crypto.SecureUtil;
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
import com.vict.framework.bean.FileInfo;
import com.vict.framework.myannotation.MyDescription;
import com.vict.framework.utils.IdUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

@Slf4j
@MyDescription("文件工具类")
public class FileUtils {

    @SneakyThrows
    public static FileInfo downloadFile(String fileUrl){
        // fileUrl = URLUtil.encode(fileUrl);
        String fileTypeName = null;
        if(fileUrl.lastIndexOf(".") >= 0){
            fileTypeName = fileUrl.substring(fileUrl.lastIndexOf(".") + 1);
        }


        URL url = new URL(fileUrl);
        URLConnection urlConnection = url.openConnection();
        InputStream inputStream = urlConnection.getInputStream();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int len = -1;
        Long fileSize = 0L;
        while ((len = inputStream.read(buffer)) != -1) {
            fileSize += len;
            byteArrayOutputStream.write(buffer, 0, len);
        }

        String tempPath = System.getProperty("java.io.tmpdir");

        String fileName = "临时文件" + IdUtils.snowflakeId();
        if(fileTypeName != null){
            fileName = fileName + "." + fileTypeName;
        }

        File file = new File(tempPath, fileName);
        try(OutputStream outputStream = new FileOutputStream(file)) {
            byteArrayOutputStream.writeTo(outputStream);
        }

        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileSize(fileSize);
        fileInfo.setFile(file);
        fileInfo.setFileName(fileName);

        String md5 = SecureUtil.md5(file);
        fileInfo.setMd5(md5);

        fileInfo.setFileType(fileTypeName);

        return fileInfo;
    }

    @MyDescription("生成临时文件")
    public File createTempFile(String fileName){
        String tempPath = System.getProperty("java.io.tmpdir");
        File file = new File(tempPath, fileName);
        return file;
    }




}
