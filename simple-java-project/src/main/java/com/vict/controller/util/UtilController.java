package com.vict.controller.util;


import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.vict.bean.util.vo.QiniuTokenVO;
import com.vict.framework.FrameworkCommon;
import com.vict.framework.bean.R;
import com.vict.framework.web.ApiPrePath;
import com.vict.utils.FileUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ApiPrePath
@Api
@Slf4j
@RestController
@RequestMapping("/util")
public class UtilController {

    @PostMapping("/qiniuToken")
    public R<QiniuTokenVO> qiniuToken(){
        String accessKey = FrameworkCommon.qiNiuAk;
        String secretKey = FrameworkCommon.qiNiuSk;
        String bucket = FrameworkCommon.qiNiuBucket;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        QiniuTokenVO qiniuTokenVO = new QiniuTokenVO();
        qiniuTokenVO.setToken(upToken);
        qiniuTokenVO.setQiNiuUploadHost(FrameworkCommon.qiNiuUploadHost);
        qiniuTokenVO.setQiNiuBucket(FrameworkCommon.qiNiuBucket);
        qiniuTokenVO.setQiNiuFileUrl(FrameworkCommon.qiNiuFileUrl);

        return R.ok(qiniuTokenVO);
    }
}
