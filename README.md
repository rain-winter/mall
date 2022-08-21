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

# validation

1. 添加依赖

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

2. 使用

~~~java
public class LoginReq {
    @NotBlank(message = "姓名不能为空")
    private String userName;

    @NotBlank(message = "密码不能为空")
    private String password;
}
~~~

~~~java
@PostMapping("/login")
public ApiRestRes login(@RequestBody @Valid LoginReq loginReq){}
~~~

## globalExceptionHandler

> 全局异常处理

~~~java
/**
 * 处理统一异常的handler
 */
/*这个注解是拦截异常的*/
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 重名用户异常在service层抛出的是异常，不是APIResponse
     * 系统级别的异常要打印出日志
     */

    /**
     * 这个注解是  告诉 处理系统异常
     *
     * @param e 当前产生的异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handleException(Exception e) {
        log.error("Default Exception", e);
        // 处理系统的异常
        return ApiRestRes.error(MallExceptionEnum.SYSTEM_ERROR);
    }

    /**
     * 拦截自定义异常并处理
     *
     * @param e MallException.class
     */
    @ExceptionHandler(MallException.class)
    @ResponseBody
    public Object handleImoocMallException(MallException e) {
        log.error("MallException" + e);
        return ApiRestRes.error(e.getCode(), e.getMessage());
    }
	// 处理参数校验异常
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

