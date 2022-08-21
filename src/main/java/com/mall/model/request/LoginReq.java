package com.mall.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginReq {
    @NotBlank(message = "姓名不能为空")
    private String userName;

    @NotBlank(message = "密码不能为空")
    private String password;
}
