package com.mall.model.request;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 创建订单 的 参数
 */
@Data
public class CreateOrderReq {
    @NotNull
    private String receiverName;

    @NotNull
    private String receiverMobile;

    @NotNull
    private String receiverAddress;

    @NotNull
    private Integer postage = 0; // 运费，默认是0

    private Integer paymentType = 1; // 扫描

}