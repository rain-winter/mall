package com.mall.model.request;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 添加目录的请求类
 */
@Data
public class UpdateCategoryReq {

    @NotNull(message = "id不能为空")
    private Integer id;

    @Size(min = 2, max = 5) // 食物名字 限制字符串最短为2，最长为5个字符
    @NotNull
    private String name;

    private String img;

    @Max(3) // 类型最大的数字是3。我们只有三级目录
    private Integer type;

    private Integer parentId;

    private Integer orderNum;
}
