//package com.mall.exception;
//
//import com.netflix.graphql.types.errors.TypedGraphQLError;
//import graphql.GraphQLError;
//import graphql.execution.DataFetcherExceptionHandler;
//import graphql.execution.DataFetcherExceptionHandlerParameters;
//import graphql.execution.DataFetcherExceptionHandlerResult;
//import org.springframework.stereotype.Component;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.CompletableFuture;
//
//@Component
//public class CustomDataFetchingExceptionHandler implements DataFetcherExceptionHandler {
//    @Override
//    public CompletableFuture<DataFetcherExceptionHandlerResult> handleException(DataFetcherExceptionHandlerParameters handlerParameters) {
//        if (handlerParameters.getException() instanceof MallException){
//            System.out.println(handlerParameters.getException());
//
//            Map<String, Object> debugInfo = new HashMap<>();
//            debugInfo.put("somefield", "somevalue");
//
//            GraphQLError graphqlError = TypedGraphQLError.newInternalErrorBuilder()
//                    .message(handlerParameters.getException().getMessage())
//                    .debugInfo(debugInfo)
//                    .path(handlerParameters.getPath()).build();
//
//            DataFetcherExceptionHandlerResult result = DataFetcherExceptionHandlerResult.newResult()
//                    .error(graphqlError)
//                    .build();
//
//            return CompletableFuture.completedFuture(result);
//        }
//        System.out.println("--------------------------------");
//        return DataFetcherExceptionHandler.super.handleException(handlerParameters);
//    }
//
//}
