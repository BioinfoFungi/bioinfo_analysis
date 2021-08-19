package com.wangyang.bioinfo.config;

import com.wangyang.bioinfo.util.BioinfoException;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class HttpConfig {
    @Value("${http.port}")
    private int httpPort;
    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addAdditionalTomcatConnectors(createStandardConnector());
        return tomcat;
    }

    private Connector createStandardConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setPort(httpPort);
        return connector;
    }

//    @Value("${https.port:}")
//    private Integer port;
//    @Value("${https.ssl.key-store:}")
//    private String key_store;
//    @Value("${https.ssl.key-store-password:}")
//    private String key_store_password;
//
////    @Bean
//    public ServletWebServerFactory servletContainer(){
//        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
//
//        if(port!=null && key_store!="" && key_store_password!=""){
//            tomcat.addAdditionalTomcatConnectors(createSslConnector());
//            return tomcat;
//        }
//        return tomcat;
//    }
//    private Connector createSslConnector() {
//        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
//        String prefix = "classpath:";
//        if(key_store.startsWith(prefix)){
//            String filename = key_store.substring(prefix.length(),key_store.length());
//            try {
//                key_store = new ClassPathResource(filename).getFile().getAbsolutePath();
//            } catch (IOException e) {
//                e.printStackTrace();
//                throw  new BioinfoException(e.getMessage());
//            }
//        }
////			File keystore = new ClassPathResource("sample.jks").getFile();
//        /*File truststore = new ClassPathResource("sample.jks").getFile();*/
//        connector.setScheme("https");
//        connector.setSecure(true);
//        connector.setPort(port);
//        protocol.setSSLEnabled(true);
//        protocol.setKeystoreFile(key_store);
//        protocol.setKeystorePass(key_store_password);
////		protocol.setKeyPass(key_password);
//        return connector;
//
//    }
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
