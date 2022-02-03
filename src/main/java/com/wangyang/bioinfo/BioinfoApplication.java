package com.wangyang.bioinfo;

import com.wangyang.bioinfo.repository.base.BaseRepositoryFactoryBean;
import com.wangyang.bioinfo.util.BioinfoException;
import com.wangyang.bioinfo.websocket.WebSocketServer;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
//import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
@EnableAsync
@EnableCaching
@EnableJpaRepositories(basePackages = {"com.wangyang.bioinfo.repository"},
		repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class//指定自己的工厂类
)
@EnableTransactionManagement
//@EnableSwagger2
//@ComponentScan("com.wangyang.bioinfo.a")

public class BioinfoApplication {

	public static void main(String[] args) {
//		System.setProperty("spring.config.additional-location",
//				"optional:file:${user.home}/.bioinfo/,optional:file:${user.home}/.bioinfo-dev/");
//		System.setProperty("spring.config.additional-location","file:${user.home}/.bioinfo/application-prod.yml");
		SpringApplication.run(BioinfoApplication.class, args);
	}

}
