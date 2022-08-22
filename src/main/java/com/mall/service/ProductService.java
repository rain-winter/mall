package com.mall.service;

import com.github.pagehelper.PageInfo;
import com.mall.model.pojo.Category;
import com.mall.model.pojo.Product;
import com.mall.model.request.AddCategoryReq;
import com.mall.model.request.AddProductReq;
import com.mall.model.request.ProductListReq;
import com.mall.model.vo.CategoryVO;
import com.mall.model.request.AddProductReq;

import java.util.List;

public interface ProductService {
    public void add(AddProductReq addProductReq);

    void update(Product updateProduct);


    void delete(Integer id);

    void batchUpdateSellStatus(Integer[] ids, Integer sellStatus);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    Product detail(Integer id);

    PageInfo list(ProductListReq productListReq);
}
