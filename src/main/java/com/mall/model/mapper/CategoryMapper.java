package com.mall.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.model.pojo.Category;

import java.util.List;

public interface CategoryMapper extends BaseMapper<Category> {
    Category selectByName(String name);

    List<Category> selectList();

    List<Category> selectCategoriesByParentId(Integer parentId);
}
