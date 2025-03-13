package com.vict.framework.web.filecache.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("file_cache")
public class FileCache {

    @ApiModelProperty("id")
    @TableId("id")
    private Long id;

    @ApiModelProperty("源码路径")
    @TableField("resource_file_uri")
    private String resourceFileUri;

    @ApiModelProperty("资源重定向url")
    @TableField("redirect_url")
    private String redirectUrl;
}
