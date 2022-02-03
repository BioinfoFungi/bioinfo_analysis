package com.wangyang.bioinfo.util;

import lombok.Data;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * @author wangyang
 * @date 2021/4/24
 */
@Data
public class BaseResponse<T> {
    public enum MsgType{
        NOTIFY,TEST_CODE
    }
    /**
     * Response status.
     */
    private Integer status;

    /**
     * Response message.
     */
    private String message;

    /**
     * Response development message
     */
    private String devMessage;

    /**
     * Response data
     */
    private T data;
    /**
     * Response data
     */
    private MsgType msgType;

    public BaseResponse(Integer status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
    public BaseResponse(Integer status, MsgType msgType, T data) {
        this.status = status;
        this.msgType = msgType;
        this.data = data;
    }
    public BaseResponse(Integer status, MsgType msgType,  String message,T data) {
        this.status = status;
        this.message =message;
        this.msgType = msgType;
        this.data = data;
    }

    public BaseResponse(Integer status, MsgType msgType,  String message) {
        this.status = status;
        this.message =message;
        this.msgType = msgType;
    }
    public BaseResponse(){}
    /**
     * Creates an ok result with message and data. (Default status is 200)
     *
     * @param data    result data
     * @param message result message
     * @return ok result with message and data
     */
    @NonNull
    public static <T> BaseResponse<T> ok(@Nullable String message, @Nullable T data) {
        return new BaseResponse<>(HttpStatus.OK.value(), message, data);
    }
    public static <T> BaseResponse<T> error(String message){
        return new BaseResponse<>(HttpStatus.UNAUTHORIZED.value(),message,null);
    }
    public static <T> BaseResponse<T> ok(@Nullable MsgType msgType, String message,@Nullable T data) {
        return new BaseResponse<>(HttpStatus.OK.value(), msgType,message, data);
    }
    public static <T> BaseResponse<T> ok(@Nullable MsgType msgType, String message) {
        return new BaseResponse<>(HttpStatus.OK.value(), msgType,message);
    }
    /**
     * Creates an ok result with message only. (Default status is 200)
     *
     * @param message result message
     * @return ok result with message only
     */
    @NonNull
    public static <T> BaseResponse<T> ok(@Nullable String message) {
        return ok(message, null);
    }

    /**
     * Creates an ok result with data only. (Default message is OK, status is 200)
     *
     * @param data data to response
     * @param <T>  data type
     * @return base response with data
     */
    public static <T> BaseResponse<T> ok(@NonNull T data) {
        return new BaseResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
    }

    public static MappingJacksonValue getOrCreateContainer(Object body) {
        return (body instanceof MappingJacksonValue ? (MappingJacksonValue) body : new MappingJacksonValue(body));
    }

    public static void beforeBodyWriteInternal(MappingJacksonValue bodyContainer,
                                         MediaType mediaType,
                                         MethodParameter methodParameter,
                                         ServerHttpRequest request,
                                         ServerHttpResponse response) {
        // Get return body
        Object returnBody = bodyContainer.getValue();

        if (returnBody instanceof BaseResponse) {
            // If the return body is instance of BaseResponse
            BaseResponse<?> baseResponse = (BaseResponse) returnBody;
            response.setStatusCode(HttpStatus.resolve(baseResponse.getStatus()));
            return;
        }

        // Wrap the return body
        BaseResponse<?> baseResponse = BaseResponse.ok(returnBody);
        bodyContainer.setValue(baseResponse);
        response.setStatusCode(HttpStatus.valueOf(baseResponse.getStatus()));
    }
}
