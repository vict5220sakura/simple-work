package com.vict.framework.web.filecache;

import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.io.resource.ResourceUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.vict.framework.FrameworkCommon;
import com.vict.framework.utils.IdUtils;
import com.vict.framework.utils.QiNiuUtils;
import com.vict.framework.utils.cache.CacheUtils;
import com.vict.framework.web.filecache.entity.FileCache;
import com.vict.framework.web.filecache.mapper.FileCacheMapper;
import com.vict.utils.FileUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;

@DependsOn("frameworkCommon")
@Slf4j
@Component
public class WebFileCache {

    private static FileCacheMapper fileCacheMapper;

    @Autowired
    public void setFileCacheMapper(FileCacheMapper fileCacheMapper){
        WebFileCache.fileCacheMapper = fileCacheMapper;
    }

    /** 查询文件缓存 */
    public static FileCache selectFileCache(String netFileUri){
        FileCache fileCache = Optional.ofNullable(CacheUtils.selectCache("webFileCache-" + netFileUri))
                .map(o -> JSONObject.parseObject(o, FileCache.class))
                .orElse(null);
        if(fileCache != null){
            return fileCache;
        }
        FileCache fileCacheDb = fileCacheMapper.selectOne(
                new LambdaQueryWrapper<FileCache>().eq(FileCache::getResourceFileUri, netFileUri)
        );
        if(fileCacheDb != null){
            // 重新构建缓存
            CacheUtils.addCache("webFileCache-" + netFileUri, JSONObject.toJSONString(fileCacheDb), null);
        }
        return fileCacheDb;
    }

    public static void createFileCahce(String netFileUri){

        FileCache fileCache1 = selectFileCache(netFileUri);

        if(fileCache1 != null){
            return;
        }

        // resourceFileUri = /simple-server/ web /index.html
        String localFileUriPath = netFileUri.replace(FrameworkCommon.serverContextPath, "static/");

        Resource resourceObj = ResourceUtil.getResourceObj(localFileUriPath);
        InputStream inputStream = resourceObj.getStream();
        // List<URL> resources = ResourceUtil.getResources();

        // if(resources != null && resources.size() == 1){
        // URL url = resources.get(0);
        // String filePath = url.getFile();

        String fileName = netFileUri;

        if(fileName.startsWith("/")){
            fileName = fileName.replaceFirst("/", "");
        }

        String redirectUrl = QiNiuUtils.uploadQiNiu(inputStream, fileName);

        FileCache fileCache = new FileCache();
        fileCache.setId(IdUtils.snowflakeId());
        fileCache.setResourceFileUri(netFileUri);
        fileCache.setRedirectUrl(redirectUrl);

        fileCacheMapper.insert(fileCache);

        CacheUtils.addCache("webFileCache-" + netFileUri, JSONObject.toJSONString(fileCache), null);
        // }
    }

    public void createFileCahceForce(String netFileUri) {
        String localFilePath = netFileUri.replace(FrameworkCommon.serverContextPath, "static/");
        Resource resourceObj = ResourceUtil.getResourceObj(localFilePath);
        InputStream inputStream = resourceObj.getStream();

        String fileName = netFileUri + IdUtils.snowflakeId();

        String redirectUrl = QiNiuUtils.uploadQiNiu(inputStream, fileName);

        fileCacheMapper.delete(new LambdaUpdateWrapper<FileCache>().eq(FileCache::getResourceFileUri, netFileUri));

        FileCache fileCache = new FileCache();
        fileCache.setId(IdUtils.snowflakeId());
        fileCache.setResourceFileUri(netFileUri);
        fileCache.setRedirectUrl(redirectUrl);

        fileCacheMapper.insert(fileCache);

        CacheUtils.addCache("webFileCache-" + netFileUri, JSONObject.toJSONString(fileCache), null);
        // }
    }

    @SneakyThrows
    @PostConstruct
    public void init(){
        if(FrameworkCommon.webStaticUseCache == false){
            return;
        }

        // web资源七牛缓存
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        org.springframework.core.io.Resource[] resourcesList = resolver.getResources("classpath*:/static/web/**/*");
        log.info("web资源数量: {}", resourcesList.length);
        for(org.springframework.core.io.Resource resource : resourcesList){
            if(resource.isReadable() == false){
                continue;
            }
            URL url = resource.getURL();
            String path = url.getPath();

            String netFileUri = FrameworkCommon.serverContextPath + "web" + path.substring(path.indexOf("static/web") + "static/web".length());

            if((FrameworkCommon.serverContextPath + "web" + "/index.html").equals(netFileUri)){
                continue;
            }

            if((FrameworkCommon.serverContextPath + "web" + "/favicon.ico").equals(netFileUri)){
                createFileCahceForce(netFileUri);
            }else{
                createFileCahce(netFileUri);
            }
        }

        // app资源上传七牛云
        org.springframework.core.io.Resource[] appResourcesList = resolver.getResources("classpath*:/static/app/**/*");
        log.info("app资源数量: {}", appResourcesList.length);
        for(org.springframework.core.io.Resource resource : appResourcesList){
            if(resource.isReadable() == false){
                continue;
            }
            URL url = resource.getURL();
            String path = url.getPath();

            String netFileUri = FrameworkCommon.serverContextPath + "app" + path.substring(path.indexOf("static/app") + "static/app".length());

            if((FrameworkCommon.serverContextPath + "app" + "/index.html").equals(netFileUri)){
                continue;
            }

            if(netFileUri.startsWith(FrameworkCommon.serverContextPath + "app/static")){
                createFileCahceForce(netFileUri);
            }else{
                createFileCahce(netFileUri);
            }
        }
    }


}
