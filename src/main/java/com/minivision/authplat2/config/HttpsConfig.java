package com.minivision.authplat2.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * HTTPS配置
 * @author hughzhao
 * @2017年5月22日
 */
@Configuration
public class HttpsConfig {
	
	@Value("${server.port}")
    private Integer httpsPort;
	
	@Value("${http.port}")
    private Integer httpPort;
	
	private static class SSLTomcatEmbeddedServletContainerFactory extends TomcatEmbeddedServletContainerFactory {
		
		protected void postProcessContext(Context context) {
            SecurityConstraint securityConstraint = new SecurityConstraint();
            securityConstraint.setUserConstraint("CONFIDENTIAL");
            SecurityCollection collection = new SecurityCollection();
            collection.addPattern("/*");
            securityConstraint.addCollection(collection);
            SecurityCollection excludeCollection = new SecurityCollection();
            excludeCollection.addPattern("/manage/*");
            securityConstraint.removeCollection(excludeCollection);
            context.addConstraint(securityConstraint);
        }
		
	}

	@Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory tomcat = new SSLTomcatEmbeddedServletContainerFactory();
        tomcat.addAdditionalTomcatConnectors(httpConnector());
        return tomcat;
    }

    @Bean
    public Connector httpConnector(){
        final Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(httpPort);//表示用8080端口来供http访问
        connector.setSecure(false);
        connector.setRedirectPort(httpsPort);//自动重定向到8443端口
        return connector;
    }
	
}
