package com.mall.model.request;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 添加目录的请求类
 */
@Data
public class AddCategoryReq {
    @Size(min = 2, max = 5) // 限制字符串最短为2，最长为5个字符
    @NotNull(message = "名字不能为空")
    private String name;

    @Max(3) // 类型最大的数字是3。我们只有三级目录
    @NotNull(message = "类型不能为空")
    private Integer type;

    @NotNull(message = "parentId不能为空")
    private Integer parentId;

    @NotNull(message = "orderNum不能为空")
    private Integer orderNum;

}
