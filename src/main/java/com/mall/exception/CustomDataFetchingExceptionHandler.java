package com.mall.exception;

import com.netflix.graphql.types.errors.TypedGraphQLError;
import graphql.GraphQLError;
import graphql.execution.DataFetcherExceptionHandler;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class CustomDataFetchingExceptionHandler implements DataFetcherExceptionHandler {

    @Override
    public CompletableFuture<DataFetcherExceptionHandlerResult> handleException(DataFetcherExceptionHandlerParameters handlerParameters) {
        if (handlerParameters.getException() instanceof MallException) {
            Map<String, Object> debugInfo = new HashMap<>();
            debugInfo.put("somefield", "somevalue");
            System.out.println("-----------------------------------");
            System.out.println(handlerParameters.getException().getMessage());
            System.out.println("-----------------------------------");


            GraphQLError graphqlError = TypedGraphQLError.newInternalErrorBuilder()
                    .message("This custom thing went wrong!")
                    .debugInfo(debugInfo)
                    .path(handlerParameters.getPath()).build();

            DataFetcherExceptionHandlerResult result = DataFetcherExceptionHandlerResult.newResult()
                    .error(graphqlError)
                    .build();

            return CompletableFuture.completedFuture(result);
        } else if (handlerParameters.getException() instanceof MethodArgumentNotValidException){
            Map<String, Object> debugInfo = new HashMap<>();
            debugInfo.put("somefield", "somevalue");
            GraphQLError graphqlError = TypedGraphQLError.newInternalErrorBuilder()
                    .message("This custom thing went wrong!")
                    .debugInfo(debugInfo)
                    .path(handlerParameters.getPath()).build();

            DataFetcherExceptionHandlerResult result = DataFetcherExceptionHandlerResult.newResult()
                    .error(graphqlError)
                    .build();

            return CompletableFuture.completedFuture(result);
        }else {
            return DataFetcherExceptionHandler.super.handleException(handlerParameters);
        }
    }
}
