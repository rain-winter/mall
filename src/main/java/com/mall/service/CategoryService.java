package com.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mall.model.pojo.Category;
import com.mall.model.request.AddCategoryReq;
import com.mall.model.vo.CategoryVO;

import java.util.List;

public interface CategoryService extends IService<Category> {
    void add(AddCategoryReq addCategoryReq);

    void update(Category updateCategory);

    void delete(Integer id);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    List<CategoryVO> listCategoryForCustomer(Integer parentId);
}
