package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.request.AddProductReq;
import com.imooc.mall.model.request.UpdateProductReq;
import com.imooc.mall.service.ProductService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

/**
 * 后台商品管理Controller
 */
@RestController
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
    public ApiRestResponse addProduct(@Valid @RequestBody AddProductReq addProductReq) {
        productService.add(addProductReq);
        return ApiRestResponse.success();
    }

    /**
     * 更新商品
     *
     * @param updateProductReq
     * @return
     */
    @PostMapping("admin/product/update")
    public ApiRestResponse updateProduct(@Valid @RequestBody UpdateProductReq updateProductReq) {
        Product product = new Product();
        BeanUtils.copyProperties(updateProductReq, product);
        productService.update(product);
        return ApiRestResponse.success();
    }

    /**
     * 删除商品
     *
     * @param id
     * @return
     */
    @PostMapping("admin/product/delete")
    public ApiRestResponse deleteProduct(@RequestParam Integer id) {
        productService.delete(id);
        return ApiRestResponse.success();
    }

    /**
     * 批量上下架商品
     *
     * @param ids
     * @param sellStatus
     * @return
     */
    @PostMapping("admin/product/batchUpdateSellStatus")
    public ApiRestResponse batchUpdateSellStatus(@RequestParam Integer[] ids, @RequestParam Integer sellStatus) {
        productService.batchUpdateSellStatus(ids, sellStatus);
        return ApiRestResponse.success();
    }

    /**
     * 后台商品列表接口
     * @param pageNum 第几页
     * @param pageSize 每页多少条
     * @return
     */
    @PostMapping("/admin/product/list")
    public ApiRestResponse list(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize) {
        PageInfo pageInfo = productService.listForAdmin(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }


    @PostMapping("admin/upload/file")
    public ApiRestResponse upload(HttpServletRequest httpServletRequest,
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
                throw new ImoocMallException(ImoocMallExceptionEnum.MKDIR_FAILED);
            }
        }
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();

        }
        try {
            return ApiRestResponse.success(getHost(new URI(httpServletRequest.getRequestURI() + "")) + "/images/" + newFileName);
        } catch (URISyntaxException e) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.UPLOAD_FAILED);
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

