package com.imooc.mall.model.dao;

import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.query.ProductListQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    Product selectByName(String name);

    //    动态sql
    int batchUpdateSellStatus(@Param("ids") Integer[] ids, @Param("sellStatus") Integer sellStatus);

    //    查询所有数据，分页用
    List<Product> selectListForAdmin();

    // 前台列表
    List<Product> selectList(@Param("query") ProductListQuery query);
}