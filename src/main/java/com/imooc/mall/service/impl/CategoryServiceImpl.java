package com.imooc.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.dao.CategoryMapper;
import com.imooc.mall.model.pojo.Category;
import com.imooc.mall.model.request.AddCategoryReq;
import com.imooc.mall.service.CategoryService;
import com.imooc.mall.vo.CategoryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public void add(AddCategoryReq addCategoryReq) {
        Category category = new Category();
        // 这个方法，把addCategoryReq的属性拷贝到category。省事，不用再写setter()
        BeanUtils.copyProperties(addCategoryReq, category);
        // 判断是否有重名的分类
        Category categoryOld = categoryMapper.selectByName(addCategoryReq.getName());
        if (categoryOld != null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
        }
        int count = categoryMapper.insertSelective(category);
        if (count == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.CREATE_FAILED);
        }
    }

    /**
     * 传递过来的参数。这个通过categoryMapper.selectByName()查询的方法，只可以返回一条数据。因为我们的数据库，不可以有重名的数据
     *
     * @param updateCategory
     */
    @Override
    public void update(Category updateCategory) {
        if (updateCategory.getName() != null) {
            Category categoryOld = categoryMapper.selectByName(updateCategory.getName());
            if (categoryOld != null && !categoryOld.getId().equals(updateCategory.getId())) {
                // 判断是否能查到数据，并且旧数据的id和传过来的数据的id是否相同
                throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
            }
        }
        int count = categoryMapper.updateByPrimaryKeySelective(updateCategory);
        if (count == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
        }
    }


    /**
     * 删除目录
     *
     * @param id
     */
    @Override
    public void delete(Integer id) {
        Category categoryOld = categoryMapper.selectByPrimaryKey(id);
        // 查不到，无法删除
        if (categoryOld == null) {

            throw new ImoocMallException(ImoocMallExceptionEnum.DELETE_FAILED);
        }
        int count = categoryMapper.deleteByPrimaryKey(id);
        if (count == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.DELETE_FAILED);
        }
    }

    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize) {
        // 通过它实现分页
        PageHelper.startPage(pageNum, pageSize, "type,order_num");
        List<Category> categoryList = categoryMapper.selectList();
        PageInfo pageInfo = new PageInfo(categoryList);
        return pageInfo;
    }

    /**
     * 给顾客的分类列表，包括子目录
     *
     * @return
     */
    @Override
    public List<CategoryVO> listCategoryForCustomer(Integer parentId) {
        ArrayList<CategoryVO> categoryVOArrayList = new ArrayList<>();
        recursivelyFindCategories(categoryVOArrayList, parentId);
        return categoryVOArrayList;
    }

    /**
     * @param categoryVOArrayList  一个List。存储数据
     * @param parentId 父id
     */
    private void recursivelyFindCategories(List<CategoryVO> categoryVOArrayList
            , Integer parentId) {
        // 递归获取所有子类别，并组合成一个“目录树”
        List<Category> categoryList = categoryMapper.selectCategoriesByParentId(parentId);
        if (!CollectionUtils.isEmpty(categoryList)) {
            // 我们直接判断categoryList==null。不太好
            // 所有我们用这个方法。它除了判断是否为null，还会判断集合里有没有元素。如果返回true代表没元素为空。
            for (int i = 0; i < categoryList.size(); i++) {
                Category category = categoryList.get(i);
                CategoryVO categoryVO = new CategoryVO();

                BeanUtils.copyProperties(category, categoryVO);
                categoryVOArrayList.add(categoryVO);
                // 递归调用recursivelyFindCategories()  为其子目录赋值
                recursivelyFindCategories(categoryVO.getChildCategory(), categoryVO.getId());
            }

        }
    }

}
