package com.vict.bean.util.vo;

import lombok.Data;

@Data
public class QiniuTokenVO {
    private String token;
    private String qiNiuUploadHost;
    private String qiNiuBucket;
    private String qiNiuFileUrl;
}
