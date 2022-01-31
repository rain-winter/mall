package com.imooc.mall.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 常量值
 */
@Component
public class Constant {
    // 加盐，用于md5加密
    public static final String SALT = "843ALNGOHANgPGHpnaphroq;agpqpq04";

    // session 中 key的值
    public static final String IMOOC_MALL_USER = "imooc_mall_user";

    // 上传文件的地址
    // @Value("${file.upload.dir}") 直接这么写报空指针异常
    public static String FILE_UPLOAD_DIR;

    @Value("${file.upload.dir}")
    public void setFileUploadDir(String fileUploadDir) {
        FILE_UPLOAD_DIR = fileUploadDir;
    }
}