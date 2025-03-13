package com.vict.framework.bean;

import lombok.Data;

import java.io.File;

@Data
public class FileInfo{
    private Long fileSize;
    private File file;
    private String md5;
    private String fileName;
    private String fileType;
}
