package com.mall.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.model.pojo.Product;
import com.mall.model.query.ProductListQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper extends BaseMapper<Product> {
//    根据名字查重
    Product selectByName(String name);
    //    动态sql
    int batchUpdateSellStatus(@Param("ids") Integer[] ids, @Param("sellStatus") Integer sellStatus);

    //    查询所有数据，分页用
    List<Product> selectListForAdmin();

    // 前台列表
    List<Product> selectList(@Param("query") ProductListQuery query);

}
