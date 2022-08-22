package com.mall.common;

import com.google.common.collect.Sets;
import com.mall.exception.MallException;
import com.mall.exception.MallExceptionEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;

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
    // @Value("${file.upload.dir}") 直接这么写报空指针异常。引文是静态的变量
    public static String FILE_UPLOAD_DIR;

    @Value("${file.upload.dir}")
    public void setFileUploadDir(String fileUploadDir) {
        FILE_UPLOAD_DIR = fileUploadDir;
        System.out.println("FILE_UPLOAD_DIR"+FILE_UPLOAD_DIR);
    }

    // 排序
    public interface ProductListOrderBy {
        // 构建 set
        HashSet<String> PRICE_ASC_DESC = Sets.newHashSet("price desc", "price asc");
    }

    // 上下架枚举
    public interface SaleStatus {
        int NOT_SALE = 0; // 下架
        int SALE = 0; // 上架
    }

    // 购物车枚举
    public interface Cart {
        int UN_CHECKED = 0; // 购物车未选中
        int CHECKED = 1; // 购物车选中
    }

    public enum OrderStatusEnum {
        CANCELED(0, "用户已取消"),
        NOT_PAID(10, "未付款"),
        PAID(20, "已付款"),
        DELIVERED(30, "已发货"),
        FINISHED(40, "交易完成");

        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        OrderStatusEnum(int code, String value) {
            this.value = value;
            this.code = code;
        }

        /**
         * 根据code找到值
         * @param code
         * @return
         */
        public static OrderStatusEnum codeOf(int code) {
            for (OrderStatusEnum orderStatusEnum : values()) {
                if (orderStatusEnum.getCode() == code) {
                    return orderStatusEnum;
                }
            }
            throw new MallException(MallExceptionEnum.NO_ENUM);
        }
    }
}