package com.imooc.mall.utils;

import com.imooc.mall.common.Constant;
import org.apache.tomcat.util.codec.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5工具
 */
public class MD5Utils {
    public static String getMD5Str(String val) throws NoSuchAlgorithmException {
        // 得到 md5工具
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        // 选择tomcat的引用 。 进行加盐（拼接上我们自己的字符）
        return Base64.encodeBase64String(md5.digest((val+ Constant.SALT).getBytes()));
    }

//    public static void main(String[] args) {
//        try {
//            String md5 = getMD5Str("1234");
//            System.out.println(md5); // 加密后的字符串 gdyb21LQTcIANtvYMT7QVQ==
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//    }
}
