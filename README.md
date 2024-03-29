IDEA必装的插件：Free MyBatis plugin Maven Helper

# 1. 日志

日志级别：error、warn、info、debug、trace
我们开发时用debug。框架开发更关注trace。
1. 排除loggin （在pom.xml ---web 配置）
2. 添加log4j2依赖
3. 编辑log4j2.xml文件
# 2. AOP统一处理web请求日志
1. 在pom.xml添加依赖 （spring-boot-starter-aop）
2. 在com.imooc.mall下建立filter包
3. 建立WebLogAspect类,用于在控制台打印请求日志

# log4j







# 3. 用户模块
1. 登录，注册，重名校验，密码加密存储，session的使用，越权校验
2. 统一返回对象，异常枚举，java异常体系，统一异常处理，更新个人信息
## 3.1 统一返回对象
1. 新建com.imooc.mall.common.ApiRestResponse.java （统一返回对象）
2. 新建com.imooc.mall.exception.ImoocMallExceptionEnum （异常枚举）
3. ApiRestResponse用到了异常枚举

## 3.2 统一异常

新建`com.imooc.mall.exception.ImoocMallException.java`

它构造方法的参数是异常枚举。

## 3.3 统一处理异常

抛出异常，直接转化为JSON的APIResponse

exception包下的GlobalExceptionHanler.java

抛出的异常含有code和message

## 3.4 密码加密

通过md5加密后的字符也可以破解。https://www.cmd5.com/

md5算法是公开的。通过1234推出密文。

本站针对md5、sha1等全球通用公开的加密算法进行反向查询，通过穷举字符组合的方式，创建了明文密文对应查询数据库，创建的记录约90万亿条，占用硬盘超过500TB，查询成功率95%以上，很多复杂密文只有本站才可查询。自2006年已稳定运行十余年，国内外享有盛誉。

所以我们进行“**加盐**”

新建common.Constan.java。里面设置常量，是一大串长且难的字符。在调用md5方法时把参数拼接。

## 3.5 登录

* 登录状态要保持

方案 ： session的实现方法：登录后。保存用户信息到session

之后在先session中获取

## 3.6 更新个人签名

## 常见问题

* 相应对象不规范
* 异常不统一处理

# 商品分类模块

分类层级：我们设置成三级。（女装 -- 当季女装 -- 内衣）

## 参数校验

~~~xml
<!--  @Valid 注解-->
<dependency>
    <groupId>javax.validation</groupId>
    <artifactId>validation-api</artifactId>
</dependency>
~~~

| 注解           | 说明           |
| -------------- | -------------- |
| @Vaild         | 需要验证       |
| @NotNull       | 非空           |
| @Max(value)    | 最大值         |
| @Size(max,min) | 字符串长度范围 |

## @Valid 参数异常捕捉

~~~java
@ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ApiRestResponse handleMethodArgNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException", e);
        //  e.getBindingResult() 得到 @Valid的验证结果
        return handleBindingResult(e.getBindingResult());

    }

    private ApiRestResponse handleBindingResult(BindingResult result) {

        // 把异常处理为对外暴漏的提示
        List<String> list = new ArrayList<>();
        if (result.hasErrors()) {
            List<ObjectError> allErrors = result.getAllErrors(); // 得到所有的错误
            for (int i = 0; i < allErrors.size(); i++) {
                ObjectError objectError = allErrors.get(i);  // // 得到每一个错误
                String message = objectError.getDefaultMessage();
                System.out.println("---------------------------------");
                System.out.println(message);
                System.out.println("---------------------------------");
                list.add(message);
            }
        }
        // 此时已经初始化了
        if (list.size() == 0) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.REQUEST_PARAM_ERROR);
        }
        return ApiRestResponse.error(ImoocMallExceptionEnum.REQUEST_PARAM_ERROR.getCode(), list.toString());

    }
~~~

## 更新目录

## 统一校验管理员身份

新建filter--AdminFilter实现`Filter`。这个类通过HttpSession判断用户是否登录，是否是管理员

新建AdminFilterConfig

~~~java
@Configuration
public class AdminFilterConfig {
    @Bean
    public AdminFilter adminFilter() {return new AdminFilter();

    @Bean(name = "adminFilterConf") // 给这个Bean设置名字
    public FilterRegistrationBean adminFilterConfig() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean<>(); // 注册过滤bean
        filterRegistrationBean.setFilter(adminFilter());
        // 设置拦截的 URL。admin/分类接口 商品接口 订单接口都要拦截
        filterRegistrationBean.addUrlPatterns("/admin/category/*");
        filterRegistrationBean.addUrlPatterns("/admin/product/*");
        filterRegistrationBean.addUrlPatterns("/admin/order/*");
        // 给当前的Bean设置名字
        filterRegistrationBean.setName("adminFilterConfig");
        // 返回
        return filterRegistrationBean;
    }
}
~~~

## 分页

添加分页依赖

~~~xml
<dependency>
   <groupId>com.github.pagehelper</groupId>
   <artifactId>pagehelper-spring-boot-starter</artifactId>
   <version>1.4.1</version>
</dependency>
~~~

在vo包里新建`CategoryVO` 它比pojo的`Category`  多了返回自己的方法

~~~java
public class CategoryVO{
    public List<CategoryVO> childCategory = new ArrayList();
}
~~~

在CategoryMapper新增`List<Category> selectList();`

在对应的xml里写查询语句

~~~xml
<select id="selectList"  resultMap="BaseResultMap">
        select
        <include refid="Base_Column_List"/>
        from imooc_mall_category
    </select>
~~~

在service层

~~~java
@Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize) {
        // 通过它实现分页
        PageHelper.startPage(pageNum, pageSize, "type,order_num");
        List<Category> categoryList = categoryMapper.selectList();
        PageInfo pageInfo = new PageInfo(categoryList);
        return pageInfo;
    }
~~~

在controller层

~~~java
@PostMapping("admin/category/list")
    @ResponseBody
    public ApiRestResponse listCategoryForAdmin(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        PageInfo pageInfo = categoryService.listForAdmin(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }
~~~
# redis

# 新增商品

## 图片上传

文件名UUID（通用唯一识别码Universally Unique Identifier）。防止重名、防止爬图。生成规则：日期和时间、MAC地址、HashCode、随机数

1. 在Constant.java定义

~~~java
// 上传文件的地址
@Value("${file.upload.dir}")
public static String FILE_UPLOAD_DIR;
~~~

2. 在application.properties定义

~~~properties
# 配置上传的路径，根据部署情况自行修改
file.upload.dir=D:\\my\\mall\\src\\main\\resources\\static\\
~~~

3. 在Product.java里编写`upload()`

~~~java
@PostMapping("admin/upload/file")
public ApiRestResponse upload(HttpServletRequest httpServletRequest, 
                              @RequestParam("file") MultipartFile file) {
    String fileName = file.getOriginalFilename(); // img2.png
    String suffixName = fileName.substring(fileName.lastIndexOf(".")); // png
    UUID uuid = UUID.randomUUID(); // 96fc163a-6e02
    String newFileName = uuid.toString() + suffixName;
    File fileDir = new File(Constant.FILE_UPLOAD_DIR);//D:\my\mall\src\main\resources\static
    File destFile = new File(Constant.FILE_UPLOAD_DIR + newFileName);
    //destFile:D:\my\mall\src\main\resources\static\96fc163a.png
    file.transferTo(destFile);
    return ApiRestResponse.success(getHost(new URI(httpServletRequest.getRequestURI() + "")) + "/images/" + newFileName);
}
~~~

**自定义静态资源映射目录**

配置`SpringBootWebMvcConfig`静态资源到本地目录

~~~java
package com.imooc.mall.config;
@Configuration
public class MallWebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + Constant.FILE_UPLOAD_DIR);
    }
}
~~~

~~~java
private URI getHost(URI uri) {
    URI effectiveURI;
    effectiveURI = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), null, null, null);
}
~~~

此时就可以通过`http://127.0.0.1:8083/images/306678cb-c495-4cc2-a212-43c89d8d4a5b.png`访问图片了。

## 批量上下架（动态sql）

* Mybatis遍历Lit
* wher语句拼接

~~~xml
<update id="batchUpdateSellStatus">
     update imooc_mall_product
     set status = #{sellStatus}
     where id in
     <foreach collection="ids" close=")" item="id" open="("
            separator=",">
         #{id}
     </foreach>
</update>
~~~

ids是我们传来的数组

## 后台商品列表接口

```java
// service层
public PageInfo listForAdmin(Integer pageNum, Integer pageSize) {
    PageHelper.startPage(pageNum, pageSize);
    // 紧接着进行查询
    List<Product> products = productMapper.selectListForAdmin(); // 查询所有的数据
    PageInfo pageInfo = new PageInfo(products);
    return pageInfo;
}
```

```java
// controller 层
@PostMapping("/admin/product/list")
public ApiRestResponse list(@RequestParam Integer pageNum,
                            @RequestParam Integer pageSize) {
    PageInfo pageInfo = productService.listForAdmin(pageNum, pageSize);
    return ApiRestResponse.success(pageInfo);
}
```

## 商品列表：搜索功能

model -- request -- ProductListReq.java

~~~java
public class ProductListReq {
    private String keyword;
    private Integer categoryId;
    private String orderBy;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
~~~

model -- query -- ProductListQuery.java

~~~java
//查询商品列表的query
public class ProductListQuery {
    private String keyword;
    private List<Integer> categoryIds; // list 存放多个id
}
~~~

ProductMapper.java

~~~java
// 前台列表
List<Product> selectList(@Param("query") ProductListQuery query);
~~~

ProductMapper.xml

~~~xml
<select id="selectList" parameterType="com.imooc.mall.model.query.ProductListQuery"
            resultMap="BaseResultMap">
        select
        <include refid="Base_Column_List"/>
        from imooc_mall_product
        <where>
            <if test="query.keyword != null">
                and name like #{query.keyword}
            </if>
            <if test="query.categoryIds != null">
                and category_id in
                <foreach collection="query.categoryIds" close=")" item="item" open="("
                         separator=",">
                    #{item}
                </foreach>
            </if>
            and status = 1
        </where>
        order by update_time desc
    </select>
~~~

service -- ProductServiceImpl.java

~~~java
public PageInfo list(ProductListReq productListReq) {
        // 构建query对象
        ProductListQuery productListQuery = new ProductListQuery();
        // 搜索 不为空
        if (!StringUtils.isNullOrEmpty(productListReq.getKeyword())) {
            String keyword = new StringBuilder().append("%").append(productListReq.getKeyword()).append("%").toString();
            // %keyword%
            productListQuery.setKeyword(keyword);
        }

        // 目录处理，如果查某个目录下的商品。要把所有子目录商品都查出来
        // 要拿到目录id的list
        if (productListReq.getCategoryId() != null) {
            List<CategoryVO> categoryVOList = categoryService.listCategoryForCustomer(productListReq.getCategoryId());
            ArrayList<Integer> categoryIds = new ArrayList<>();
            categoryIds.add(productListReq.getCategoryId());
            getCategoryIds(categoryVOList, categoryIds);
            productListQuery.setCategoryIds(categoryIds); // 所有id放到productListQuery

        }

        // 排序处理
        String orderBy = productListReq.getOrderBy();
        if (Constant.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy
        )) {
            PageHelper.startPage(productListReq.getPageNum(), productListReq.getPageSize(), orderBy);
        } else {
            PageHelper.startPage(productListReq.getPageNum(), productListReq.getPageSize());
        }
        List<Product> productList = productMapper.selectList(productListQuery);
        PageInfo pageInfo = new PageInfo(productList);
        return  pageInfo;
    }

    /**
     * @param categoryVOList 目录树，所有id放到categoryIds
     * @param categoryIds    [1,2,3]
     */
    private void getCategoryIds(List<CategoryVO> categoryVOList, ArrayList<Integer> categoryIds) {
        for (int i = 0; i < categoryVOList.size(); i++) {
            CategoryVO categoryVO = categoryVOList.get(i);
            if (categoryVO != null) {
                categoryIds.add(categoryVO.getId());
                getCategoryIds(categoryVO.getChildCategory(), categoryIds);
            }
        }
    }
~~~

# 购物车模块

## 概览

1. 添加到购物车

## 用户过滤器开发

~~~java
package com.imooc.mall.filter;
public class UesrFilter implements Filter {}
~~~

~~~java
package com.imooc.mall.config;
public class UserFilterConfig {}
~~~

## 添加购物车

~~~sql
// package com.imooc.mall.model.dao;
Cart selectCartByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);
~~~

~~~java
package com.imooc.mall.service.impl;
public List<CartVO> add(Integer userId, Integer productId, Integer count) {
    // 从用户过滤器中获取userId，
    cartService.add(UserFilter.currentUser.getId(), productId, count);
    // 1.这个商品之前不在购物车里，需要新增记录
    // 2.这个商品已经在购物车里，则数量相加
}
~~~

~~~java
package com.imooc.mall.controller;
public ApiRestResponse add(@RequestParam Integer productId, @RequestParam Integer count) {}
~~~

## select选中接口

选中和全选都是调用这个sql，只是全选不传递productId。

把用户选中得商品全部查出来

~~~sql
 <update id="selectOrNot" parameterType="map">
        update imooc_mall_cart
        set selected = #{selected}
        where user_id = #{userId}
        <if test="productId != null">
            and product_id = #{productId}
        </if>
</update>
~~~



# 订单模块

## 下单

登录、浏览商品、加入购物车、下单（取消订单）、扫码支付、发货、收获、订单完结

## 创建订单

~~~java
@Transactional(rollbackFor = Exception.class) // 有异常回滚，不插入数据库
public String create(CreateOrderReq createOrderReq) {
    Integer userId = UserFilter.currentUser.getId(); // 获取用户id
    List<CartVO> cartVOList = cartService.list(userId); // 获取用户选中的 商品列表
    // 判断商品是不是在售。通过循环cartVOList，判断Selected==1？
    // 上下架状态、库存、数量是否合规，也是通过循环
    List<OrderItem> orderItemList = cartVOListToOrderItemList(cartVOList); // orderItem对象
    // 循环，扣除该商品的库存
    cleanCart(cartVOList); // 删除购物车里已购买的商品
    String orderNo = OrderCodeFactory.getOrderCode(Long.valueOf(userId)); // 生成订单号
    orderMapper.insertSelective(order); // 插入到order表
    orderItemMapper.insertSelective(orderItem); // 循环插入orderItem表    
}
~~~

## 查看订单详情

~~~java
~~~

## 生成二维码

添加依赖

~~~xml
<dependency>
    <groupId>com.google.zxing</groupId>
    <artifactId>javase</artifactId>
    <version>3.5.0</version>
</dependency>
~~~

~~~java
public class QrcodeGenerator {
    public static void generateQRCodeImage(String text, int width, int height, String filePath) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    public static void main(String[] args) throws IOException, WriterException {
        generateQRCodeImage("hello word",200,200, "D:\\\\File\\\\GitHub\\\\mall\\\\src\\\\main\\\\resources\\\\static\\\\QRTest.png");
    }
}

~~~

在`application.properties`定义路径

~~~properties
# application.properties
file.upload.ip=127.0.0.1
~~~

在`orderServiceImpl`取值

~~~java
/**
 * 描述: 订单 Service实现类
 */
@Service
public class OrderServiceImpl implements OrderService {
    /**
     * 生成二维码
     *
     * @param orderNo 订单号
     */
    @Override
    public String qrcode(String orderNo) {
        Boolean orderIsExist = orderIsExist(orderNo);
        if (!orderIsExist) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NO_ORDER);
        }
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        // 这个ip知识和简单的网络环境（只是用wifi、4G）像是蓝牙就不太准确
        // 这个ip是本机局域网。这个ip可以用手机扫码。上线后换成自己的ip
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        String address = ip + ":" + request.getLocalPort();
        String payUrl = "http://" + address + "/pay?orderNo=" + orderNo; // http://127.0.0.1/8083/pay?orderNo=orderNo
        String pngAddress;
        try {
            QrcodeGenerator.generateQRCodeImage(payUrl, 350, 350, Constant.FILE_UPLOAD_DIR + orderNo + ".png");
            pngAddress = "http://" + address + "/images/" + orderNo + ".png"; //  // http://127.0.0.1//images/orderNo.png
        } catch (WriterException | IOException e) {
            throw new RuntimeException(e);
        }
        return pngAddress;
    }
}
~~~

### 获取局域网ip

~~~java
// 这个ip可以用手机扫描
ip = InetAddress.getLocalHost().getHostAddress();
~~~

# 阿里云部署

~~~java
package com.imooc.mall.config;

import com.imooc.mall.common.Constant;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MallWebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //  以admin/**底下的文件定向到 /static/admin/
        // 这里配置的是后台管理系统
        registry.addResourceHandler("/admin/**").addResourceLocations("classpath:/static/admin/");
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + Constant.FILE_UPLOAD_DIR);
    }
}
~~~

~~~bash
set password for root@localhost = password('123456');
~~~

0.0.0.0/0

~~~bash
nohup java -jar -Dserver.port= 8081 -
Dspring.profiles.active= prod /root/mall-0.0.1-
SNAPSHOT.jar > /root/null2>&1 &
rebuild一下
~~~

