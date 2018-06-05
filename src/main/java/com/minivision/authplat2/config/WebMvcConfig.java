package com.minivision.authplat2.config;

import javax.annotation.PostConstruct;
import javax.servlet.MultipartConfigElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import com.minivision.authplat2.interceptor.AjaxInterceptor;

/**
 * Web MVC配置
 * @author hughzhao
 * @2017年5月22日
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	
	@Value("${file.temp.dir}")
    private String tempDir;

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.mediaType("json", MediaType.APPLICATION_JSON);
		configurer.mediaType("text", MediaType.APPLICATION_JSON);
		configurer.mediaType("xml", MediaType.APPLICATION_XML);
		configurer.defaultContentType(MediaType.APPLICATION_JSON);
		configurer.ignoreAcceptHeader(false);
	}

	@PostConstruct
	public void init() {
		templateEngine.addDialect(new SpringSecurityDialect());
	}

	@Autowired
	private SpringTemplateEngine templateEngine;

	@Autowired
	private ThymeleafViewResolver thymeleafViewResolver;

	@Autowired
	private AjaxInterceptor ajaxInterceptor;

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.enableContentNegotiation(new MappingJackson2JsonView());
		registry.viewResolver(thymeleafViewResolver);
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("login/login");
		registry.addViewController("/").setViewName("redirect:/sysinfo");
		super.addViewControllers(registry);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(ajaxInterceptor);
	}

	/**
	 * 文件上传临时路径
	 */
	@Bean
	MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setLocation(tempDir);
		return factory.createMultipartConfig();
	}

}
