package com.mall.controller;

import com.github.pagehelper.PageInfo;
import com.mall.common.ApiRestRes;
import com.mall.common.Constant;
import com.mall.exception.MallExceptionEnum;
import com.mall.model.pojo.Category;
import com.mall.model.pojo.User;
import com.mall.model.request.AddCategoryReq;
import com.mall.model.request.UpdateCategoryReq;
import com.mall.model.vo.CategoryVO;
import com.mall.service.CategoryService;
import com.mall.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;


/**
 * 目录Controller
 */
@Controller
public class CategoryController {
    @Autowired
    UserService userService;
    @Autowired
    CategoryService categoryService;

    /**
     * 后台添加目录
     *
     * @param session
     * @param addCategoryReq
     * @return
     */
    @PostMapping("admin/category/add")
    @ResponseBody
    public ApiRestRes addCategory(HttpSession session, @Valid @RequestBody AddCategoryReq addCategoryReq) {
        User currentUser = (User) session.getAttribute(Constant.IMOOC_MALL_USER);
        if (currentUser == null) {
            return ApiRestRes.error(MallExceptionEnum.NEED_LOGIN);
        }
        // 校验是否是管理员
        boolean adminRole = userService.checkAdminRole(currentUser);
        if (!adminRole) {
            return ApiRestRes.error(MallExceptionEnum.NEED_LOGIN);
        }
        /* 是管理员 */
        categoryService.add(addCategoryReq);
        return ApiRestRes.success();
    }

    /**
     * 后台更新目录 加了@RequestBody 在apifox里要用json格式传递
     *
     * @param updateCategoryReq
     * @param session
     * @return
     */
    @PostMapping("admin/category/update")
    @ResponseBody
    public ApiRestRes updateCategory(@Valid @RequestBody UpdateCategoryReq updateCategoryReq, HttpSession session) {
        User currentUser = (User) session.getAttribute(Constant.IMOOC_MALL_USER);
        if (currentUser == null) {
            return ApiRestRes.error(MallExceptionEnum.NEED_LOGIN);
        }
        // 校验是否是管理员
        boolean adminRole = userService.checkAdminRole(currentUser);
        if (!adminRole) {
            return ApiRestRes.error(MallExceptionEnum.NEED_LOGIN);
        }
        /* 是管理员 */
        Category category = new Category();
        BeanUtils.copyProperties(updateCategoryReq, category);
        categoryService.update(category);
        return ApiRestRes.success();
    }

    /**
     * 后台删除目录  @RequestBody通过JSON传递。@RequestParam可以通过form-data传递
     *
     * @param id
     * @return
     */
    @PostMapping("admin/category/delete")
    @ResponseBody
    public ApiRestRes deleteCategory(@RequestParam Integer id) {
        categoryService.delete(id);
        return ApiRestRes.success();
    }

    /**
     * @param pageNum  第几页
     * @param pageSize 多少条数据
     * @return
     */
    @PostMapping("admin/category/list")
    @ResponseBody
    public ApiRestRes listCategoryForAdmin(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        PageInfo pageInfo = categoryService.listForAdmin(pageNum, pageSize);
        return ApiRestRes.success(pageInfo);
    }

    /**
     * 用户看的分类列表
     * @return
     */
    @PostMapping("/category/list")
    @ResponseBody
    public ApiRestRes listCategoryForCustomer() {
        List<CategoryVO> categoryVOS = categoryService.listCategoryForCustomer(0);
        return ApiRestRes.success(categoryVOS);
    }
}
