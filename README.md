# mal

一个java的商城项目

# graphql

这里我们使用`DGS`来集成`graphql`。[DGS]: https://netflix.github.io/dgs/

1. 添加依赖

~~~xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.netflix.graphql.dgs</groupId>
            <artifactId>graphql-dgs-platform-dependencies</artifactId>
            <!-- The DGS BOM/platform dependency. This is the only place you set version of DGS -->
            <version>4.9.16</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>com.netflix.graphql.dgs</groupId>
        <artifactId>graphql-dgs-spring-boot-starter</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
~~~

2. 在`resources/schema/schema`中建立`schema.graphqls`

~~~tsx
type Query {
    hello(name:String): String
}

type Mutation {
    login(loginInput:LoginInput):User
}
~~~

3. 在java包下建立`fetcher`包，建立`UserFetcher.java`

~~~java
@DgsComponent
public class UserFetcher {
    @DgsQuery
    public String hello(@NotNull @InputArgument("name") String name) {
        return "你好" + name;
    }
    @DgsMutation
    public User login(@InputArgument @Valid LoginInput loginInput) {
        user = userService.login(loginInput.getUserName(), loginInput.getPassword());
        return user;
    }
}
~~~

4. 访问`http://localhost:8083/graphiql?`

~~~json
{
  hello(name:"33ff33")
}
mutation {
  login(loginInput: { userName: "raddin",password:"123"}) {
    id
    email
    username
  }
}
~~~

5. 定义异常

这个方法只可以捕获到`MallException`和`RunTimeException`。对于validation注解得异常捕获不到

~~~java
@Component
public class GraphqlExceptionHandler implements DataFetcherExceptionHandler {
    @Override
    public CompletableFuture<DataFetcherExceptionHandlerResult> handleException(DataFetcherExceptionHandlerParameters handlerParameters) {
        if (handlerParameters.getException() instanceof MallException){
            System.out.println(handlerParameters.getException());

            Map<String, Object> debugInfo = new HashMap<>();
            debugInfo.put("somefield", "somevalue");

            GraphQLError graphqlError = TypedGraphQLError.newInternalErrorBuilder()
                    .message(handlerParameters.getException().getMessage())
                    .debugInfo(debugInfo)
                    .path(handlerParameters.getPath()).build();

            DataFetcherExceptionHandlerResult result = DataFetcherExceptionHandlerResult.newResult()
                    .error(graphqlError)
                    .build();

            return CompletableFuture.completedFuture(result);
        }
        System.out.println("--------------------------------");
        return DataFetcherExceptionHandler.super.handleException(handlerParameters);
    }
}
~~~



# sa-token

[官网]: https://sa-token.dev33.cn/

# validation

## 依赖

~~~xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
~~~

> 注：从 `springboot-2.3`开始，校验包被独立成了一个 `starter`组件，所以需要引入validation和web，而 `springboot-2.3`之前的版本只需要引入 web 依赖就可以了。

## @RequestBody @Valid

> @RequestBody验证，需要在DTO层使用注解，控制器层的方法加上`@Valid`开启异常校验。最后在GlobalExceptionHandler进行异常拦截

1. DTO层，使用注解

~~~java
@Data
public class LoginInput {
    @NotEmpty(message = "用户名不能为空")
    private String userName;

    @NotEmpty(message = "密码不能为空")
    @Min(4)
    @Max(8)
    private String password;
}
~~~

2. 控制器层

~~~java
@PostMapping("/login")
public ApiRestRes login(@RequestBody @Valid LoginReq loginReq) {}
~~~

3. 全局异常拦截，**这里只拦截@RequestBody @Valid产生的异常**

~~~java
@ControllerAdvice // 拦截异常的注解
public class GlobalExceptionHandler {
    /**
     * 拦截的是 @RequestBody 产生的异常
     * @param e MethodArgumentNotValidException.class
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ApiRestRes handleMethodArgNotValidException(MethodArgumentNotValidException e) {
        return handleBindingResult(e.getBindingResult());
    }

    private ApiRestRes handleBindingResult(BindingResult bindingResult) {
        List<String> list = new ArrayList<>();
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            for (ObjectError objectError : allErrors) {
                String message = objectError.getDefaultMessage();
                list.add(message);
            }
        }
        if (list.size() == 0) {
            return ApiRestRes.error(MallExceptionEnum.REQUEST_PARAM_ERROR);
        }
        return ApiRestRes.error(MallExceptionEnum.REQUEST_PARAM_ERROR.getCode(), list.toString());
    }
}
~~~

## @RequestParam @NotBlank

> **这个requestParam不能验证数字类型**

> 它产生的异常是`ConstraintViolationException`

1. 在控制器上加上**@Validated**

~~~java
@RestController
@Validated // 必须加上这个注解才可以验证
public class UserController {
    public ApiRestRes register(@RequestParam("userName") @NotBlank(message = "用户名不能为空") String userName){}
}
~~~

2. 全局异常拦截

~~~java
//处理请求参数格式错误 @RequestParam上validate失败后抛出的异常是javax.validation.ConstraintViolationException
@ExceptionHandler(ConstraintViolationException.class)
@ResponseBody
public ApiRestRes ConstraintViolationExceptionHandler(ConstraintViolationException e) {
   String message = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining());
        return ApiRestRes.error(MallExceptionEnum.REQUEST_PARAM_ERROR.getCode(),message);
}
~~~

# mybatis-plus

[官网]: https://baomidou.com/

~~~xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>最新版本</version>
</dependency>
~~~

1. `com.mall.model.pojo.User.java`是我们得实体类

~~~java
// lombok 插件。使用该注解便可以实现setter/getter/
@Data
@TableName("mall_user") // 设置表明
public class User {
    @TableId(type = IdType.AUTO) // 设置主键递增
    private Integer id;
    private String username;
}
~~~

2. `com.mall.model.mapper.UserMapper`。UserMapper继承了BaseMapper，就有了CURD的能力。

~~~java
public interface UserMapper extends BaseMapper<User> {
    // 自己得方法
    User selectLogin(@Param("userName") String userName, @Param("password") String password);
}
~~~

3. 如果没有指定mapperXML的路径，那么它就在`resources/mapper`下面

~~~xml
<!-- UserMapper.xml -->
<?xml version="1.0" encoding="utf-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.mall.model.mapper.UserMapper">
       <select id="selectLogin" parameterType="map" resultMap="BaseResultMap">
        select
        <include refid="Base_Column_List"/>
        from mall_user
        where username = #{userName,jdbcType=VARCHAR}
        and password = #{password}
    </select>
</mapper>
~~~

4. `Application`中指定mapper的路径

~~~java
@MapperScan("com.mall.model.mapper")
~~~

# log4j2

1. log4j2的依赖引入

~~~xml
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-log4j2</artifactId>
</dependency>
~~~

2. 排除springboot内置得日志组件

~~~xml
<!-- 排除 Spring-boot-starter 默认的日志配置 这种其实是错误的 排除不干净 -->
<!-- boot2.6应该可以这样。但是boot2.7不可以 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-logging</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
~~~

~~~xml
<!-- 排除 Spring-boot-starter 默认的日志配置 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-logging</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
~~~

3. 放在`resources`低下得`log4j2.xml`

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="fatal">
    <Properties>
        <!--  ${sys:user.home}/logs 配置日志路径 -->
        <Property name="baseDir" value="D:\File\Github\mall-dev\src\logs"/>
    </Properties>
</Configuration>
~~~

如果`log4j2.xml`没在这里需要在application里配置

~~~js
logging:
  config:
    classpath: log4j2.xml
~~~

# 文件上传

1. 在`application.yaml`中定义文件上传的地址

~~~yaml
file:
  upload:
    dir: D:/File/Github/mall-dev/src/main/resources/static/upload/
    ip: 0.0.0.0
~~~

2. **spring boot 2.7**需要在`application.yaml`中定义这个，不然不能显示图片

~~~yaml
spring:
  web:
    resources:
      static-locations: classpath:/static/
~~~

3. 在控制器层写方法

~~~java
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
~~~

4. 在`MallWebMvcConfig`加入代码

~~~java
public class MallWebMvcConfig extends WebMvcConfigurationSupport {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //  以admin/**底下的文件定向到 /static/admin/
        registry.addResourceHandler("/admin/**").addResourceLocations("classpath:/static/admin/");
        // 这里配置的是 显示图片
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + Constant.FILE_UPLOAD_DIR);
    }

    /**
     * 跨域
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")  //设置允许跨域访问的路径
                .allowedOriginPatterns("*")  //设置允许跨域访问的源
                .allowedMethods("*")  //允许跨域请求的方法
                .maxAge(168000)  //预检间隔时间
                .allowedHeaders("*")  //允许头部设置
                .allowCredentials(true);  //是否发送 cookie
    }
}

~~~

