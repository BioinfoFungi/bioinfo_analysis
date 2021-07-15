package com.wangyang.bioinfo.config;

import com.wangyang.bioinfo.util.BaseResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author wangyang
 * @date 2021/4/24
 * ref:https://blog.csdn.net/yanmh007/article/details/88871705
 */
@ControllerAdvice("com.wangyang.bioinfo.web")
public class CommonResponse implements ResponseBodyAdvice<Object> {


    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return AbstractJackson2HttpMessageConverter.class.isAssignableFrom(aClass);
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        MappingJacksonValue container = BaseResponse.getOrCreateContainer(o);
        BaseResponse.beforeBodyWriteInternal(container, mediaType, methodParameter, serverHttpRequest, serverHttpResponse);
        return container;
    }

}
