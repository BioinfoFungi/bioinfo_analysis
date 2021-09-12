package com.wangyang.bioinfo.pojo.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.wangyang.bioinfo.service.ICancerService;
import com.wangyang.bioinfo.service.IStudyService;
import com.wangyang.bioinfo.service.base.IBaseTermService;
import com.wangyang.bioinfo.service.impl.CancerServiceImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.EnumSet;


public enum TermType  {

    CANCER(0,"CANCER", CancerServiceImpl.class);
    private final  String name;
    private final  Class<?> service;
    private final   int code;
    @Autowired
    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Component
    public static class ReportTypeServiceInjector {
        @Autowired
        private ApplicationContext applicationContext;

        @PostConstruct
        public void postConstruct() {
            for (TermType termType : EnumSet.allOf(TermType.class)){
                termType.setApplicationContext(applicationContext);
            }
        }
    }

    TermType(int code,String name,  Class<?> service) {
        this.name = name;
        this.code=code;
        this.service=service;
    }
    public IBaseTermService getService(){
          return (IBaseTermService) applicationContext.getBean(service);
//        return null;
    }
    public Integer getCode() {
        return code;
    }
    @JsonValue
    public String getValue() {
        return name;
    }


}
