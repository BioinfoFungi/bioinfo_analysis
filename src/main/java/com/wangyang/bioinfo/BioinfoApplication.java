package com.wangyang.bioinfo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableAsync
//@EnableSwagger2
//@ComponentScan("com.wangyang.bioinfo.a")
public class BioinfoApplication {

	public static void main(String[] args) {
		System.setProperty("spring.config.additional-location","file:${user.home}/.bioinfo/application.yml");
		SpringApplication.run(BioinfoApplication.class, args);
	}

}
