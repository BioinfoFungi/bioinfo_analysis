package com.wangyang.bioinfo.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wangyang.bioinfo.interceptor.BioInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.socket.config.annotation.EnableWebSocket;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author wangyang
 * @date 2021/4/24
 */
@Configuration
public class MvcConfig   extends WebMvcConfigurationSupport  {
    @Value("${bioinfo.workDir}")
    private String workDir;

//    @Bean
//    public CacheManager cacheManager() {
//        SimpleCacheManager cacheManager = new SimpleCacheManager();
//        List<Cache> caches = new ArrayList<Cache>();
//        ConcurrentMapCache actionsBycasId = new ConcurrentMapCache("TERM");
//        caches.add(actionsBycasId);
//        cacheManager.setCaches(caches);
//        return cacheManager;
//    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("file:"+workDir+"/")//file:/home/wy/.bioinfo/
                .addResourceLocations("classpath:/static/html/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");;
        super.addResourceHandlers(registry);
    }

    /**
     * 解决拦截器在spring context创建之前完成加载
     * @return
     */
    @Bean
    public BioInterceptor bioInterceptor(){
        return new BioInterceptor();
    }
    /**
     * 配置拦截器
     * @param registry
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(bioInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/api/user/login", "/logout/**", "/loginPage/**", "/error/**",
                        "/doc.html","/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**");
    }

    /**
     * 将有ResponseBody注解的返回对象转换为json格式
     * @param converters
     */
    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        /**
         * 序列换成Json时,将所有的Long变成String
         * 因为js中得数字类型不能包括所有的java Long值
         */
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);

        // 所有的double类型返回保留三位小数
//        simpleModule.addSerializer(Double.class, CustomerDoubleSerialize.instance);
//        simpleModule.addSerializer(Double.TYPE, CustomerDoubleSerialize.instance);

        /**
         * 日期格式化
         * yyyy-MM-dd HH:mm:ss
         * https://blog.csdn.net/kylin_tam/article/details/106683981
         */
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        /**
         * 输入时间反序列化
         */
        JavaTimeModule javaTimeModule=new JavaTimeModule();
        javaTimeModule.addDeserializer(Date.class,jsonDateDeserializer());
        objectMapper.registerModule(javaTimeModule);

        objectMapper.registerModule(simpleModule);
        jackson2HttpMessageConverter.setObjectMapper(objectMapper);

        converters.add(jackson2HttpMessageConverter);
//        StringHttpMessageConverter converter = new StringHttpMessageConverter(
//                Charset.forName("UTF-8"));
//        converters.add(responseBodyConverter());
    }

    /**
     * 时间字符串参数转化
     * https://blog.csdn.net/weixin_34290096/article/details/92334617
     * @return
     */
    @Bean
    public JsonDateDeserializer jsonDateDeserializer() {
        return new JsonDateDeserializer();
    }

    /**
     * 解决Pageable pageable不能作为参数的问题
     * @param argumentResolvers
     */
    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add( new PageableHandlerMethodArgumentResolver());
    }


    @Override
    protected void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**").
                    allowedOriginPatterns("*").
                    allowedMethods("*"). //允许任何方法（post、get等）
                    allowedHeaders("*"). //允许任何请求头
                    allowCredentials(true); //带上cookie信息
    }


//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        //springwebsocket 4.1.5版本前默认支持跨域访问，之后的版本默认不支持跨域，需要设置.setAllowedOrigins("*")
//        registry.addHandler(webSocketHandler(),"/websocket/socketServer.do")
//                .setAllowedOrigins("*")
//                .addInterceptors(new SpringWebSocketHandlerInterceptor());
////
////        registry.addHandler(webSocketHandler(), "/sockjs/socketServer.do")
////                .addInterceptors(new SpringWebSocketHandlerInterceptor()).withSockJS();
//    }
//
//    @Bean
//    public TextWebSocketHandler webSocketHandler() {
//        return new SpringWebSocketHandler();
//    }
//


}
