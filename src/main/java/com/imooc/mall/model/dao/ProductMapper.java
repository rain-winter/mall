package com.imooc.mall.model.dao;

import com.imooc.mall.model.pojo.Product;

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
    int batchUpdateSellStatus(Integer[] ids, Integer sellStatus);

    //    查询所有数据，分页用
    List<Product> selectListForAdmin();
}