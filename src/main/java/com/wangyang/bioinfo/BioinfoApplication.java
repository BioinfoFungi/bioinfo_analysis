package com.wangyang.bioinfo;

import com.wangyang.bioinfo.util.BioinfoException;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
@EnableAsync
//@EnableSwagger2
//@ComponentScan("com.wangyang.bioinfo.a")
public class BioinfoApplication {

	public static void main(String[] args) {
		System.setProperty("spring.config.additional-location","file:${user.home}/.bioinfo/application.yml");
		SpringApplication.run(BioinfoApplication.class, args);
	}
	@Value("${https.port:}")
	private Integer port;
	@Value("${https.ssl.key-store:}")
	private String key_store;
	@Value("${https.ssl.key-store-password:}")
	private String key_store_password;

	@Bean
	public   ServletWebServerFactory servletContainer(){
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();

		if(port!=null && key_store!="" && key_store_password!=""){
			tomcat.addAdditionalTomcatConnectors(createSslConnector());
			return tomcat;
		}
		return tomcat;
	}
	private Connector createSslConnector() {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
		String prefix = "classpath:";
		if(key_store.startsWith(prefix)){
			String filename = key_store.substring(prefix.length(),key_store.length());
			try {
				key_store = new ClassPathResource(filename).getFile().getAbsolutePath();
			} catch (IOException e) {
				e.printStackTrace();
				throw  new BioinfoException(e.getMessage());
			}
		}
//			File keystore = new ClassPathResource("sample.jks").getFile();
		/*File truststore = new ClassPathResource("sample.jks").getFile();*/
		connector.setScheme("https");
		connector.setSecure(true);
		connector.setPort(port);
		protocol.setSSLEnabled(true);
		protocol.setKeystoreFile(key_store);
		protocol.setKeystorePass(key_store_password);
//		protocol.setKeyPass(key_password);
		return connector;

	}
//	@Bean
//	public ServletWebServerFactory servletContainer() {
//		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
//			@Override
//			protected void postProcessContext(Context context) {
//				SecurityConstraint securityConstraint = new SecurityConstraint();
//				securityConstraint.setUserConstraint("CONFIDENTIAL");
//				SecurityCollection collection = new SecurityCollection();
//				collection.addPattern("/*");
//				securityConstraint.addCollection(collection);
//				context.addConstraint(securityConstraint);
//			}
//		};
//		tomcat.addAdditionalTomcatConnectors(redirectConnector());
//		return tomcat;
//	}
//
//	private Connector redirectConnector() {
//		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//		connector.setScheme("http");
////		connector.setPort(8080);
//		connector.setSecure(false);
//		connector.setRedirectPort(8080);
//		return connector;
//	}
}
