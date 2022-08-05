package com.imooc.mall.model.request;
import javax.validation.constraints.NotNull;

/**
 * 创建订单 的 参数
 */
public class CreateOrderReq {
    @NotNull
    private String receiverName;

    @NotNull
    private String receiverMobile;

    @NotNull
    private String receiverAddress;

    @NotNull
    private Integer postage = 0;

    private Integer paymentType = 1; // 扫描

    public CreateOrderReq(String receiverName, String receiverMobile, String receiverAddress, Integer postage, Integer paymentType) {
        this.receiverName = receiverName;
        this.receiverMobile = receiverMobile;
        this.receiverAddress = receiverAddress;
        this.postage = postage;
        this.paymentType = paymentType;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public Integer getPostage() {
        return postage;
    }

    public void setPostage(Integer postage) {
        this.postage = postage;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }


}