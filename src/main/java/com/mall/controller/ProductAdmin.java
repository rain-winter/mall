package com.mall.controller;

import com.github.pagehelper.PageInfo;
import com.mall.common.ApiRestRes;
import com.mall.common.Constant;
import com.mall.exception.MallException;
import com.mall.exception.MallExceptionEnum;
import com.mall.model.pojo.Product;
import com.mall.model.request.AddProductReq;
import com.mall.model.request.UpdateProductReq;
import com.mall.service.ProductService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

/**
 * 后台商品管理Controller
 */
@RestController
@Validated // 必须加上这个注解才可以验证
public class ProductAdmin {
    @Autowired
    ProductService productService;

    /**
     * 添加商品的接口
     *
     * @param addProductReq
     * @return
     */
    @PostMapping("admin/product/add")
    public ApiRestRes addProduct(@Valid @RequestBody AddProductReq addProductReq) {
        productService.add(addProductReq);
        return ApiRestRes.success();
    }

    /**
     * 更新商品
     *
     * @param updateProductReq
     * @return
     */
    @PostMapping("admin/product/update")
    public ApiRestRes updateProduct(@Valid @RequestBody UpdateProductReq updateProductReq) {
        Product product = new Product();
        BeanUtils.copyProperties(updateProductReq, product);
        productService.update(product);
        return ApiRestRes.success();
    }

    /**
     * 删除商品
     *
     * @param id
     * @return
     */
    @PostMapping("admin/product/delete")
    public ApiRestRes deleteProduct(@RequestParam Integer id) {
        productService.delete(id);
        return ApiRestRes.success();
    }

    /**
     * 批量上下架商品
     *
     * @param ids 多个ID
     * @param sellStatus 商品上架状态：0-下架，1-上架
     */
    @PostMapping("admin/product/batchUpdateSellStatus")
    public ApiRestRes batchUpdateSellStatus(@RequestParam Integer[] ids, @RequestParam Integer sellStatus) {
        productService.batchUpdateSellStatus(ids, sellStatus);
        return ApiRestRes.success();
    }

    /**
     * 后台商品列表接口
     * @param pageNum 第几页
     * @param pageSize 每页多少条
     * @return
     */
    @PostMapping("/admin/product/list")
    public ApiRestRes list(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        PageInfo pageInfo = productService.listForAdmin(pageNum, pageSize);
        return ApiRestRes.success(pageInfo);
    }


    @PostMapping("admin/upload/file")
    public ApiRestRes upload(HttpServletRequest httpServletRequest,
                                  @RequestParam("file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        System.out.println("fileName:" + fileName);
        String suffixName = fileName.substring(fileName.lastIndexOf(".")); // 截取。后面的后缀
        // 生成文件名UUID
        UUID uuid = UUID.randomUUID();
        String newFileName = uuid.toString() + suffixName;
        // 创建文件  fileDir 是文件夹的名字
        File fileDir = new File(Constant.FILE_UPLOAD_DIR);
        System.out.println("Constant.FILE_UPLOAD_DIR:" + Constant.FILE_UPLOAD_DIR);
        System.out.println("fileDir:" + fileDir);
        File destFile = new File(Constant.FILE_UPLOAD_DIR + newFileName);
        System.out.println("destFile:" + destFile);
        if (!fileDir.exists()) {
            if (!fileDir.mkdir()) {
                throw new MallException(MallExceptionEnum.MKDIR_FAILED);
            }
        }
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();

        }
        try {
            return ApiRestRes.success(getHost(new URI(httpServletRequest.getRequestURI() + "")) + "/images/" + newFileName);
        } catch (URISyntaxException e) {
            return ApiRestRes.error(MallExceptionEnum.UPLOAD_FAILED);
        }
    }

    private URI getHost(URI uri) {
        URI effectiveURI;
        try {
            effectiveURI = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), null, null, null);
        } catch (URISyntaxException e) {
            effectiveURI = null;
        }
        return effectiveURI;
    }
}

