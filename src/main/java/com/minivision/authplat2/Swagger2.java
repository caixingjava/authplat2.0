package com.minivision.authplat2;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger配置
 * @author hughzhao
 * @2017年5月22日
 */
@Configuration
@EnableSwagger2
public class Swagger2 {

	@Bean
	public Docket createRestApi() {
		List<ResponseMessage> responses = Arrays.asList(
				new ResponseMessageBuilder() 
				.code(400)
				.message("请求参数错误(Bad Request)")
				.build(),
				new ResponseMessageBuilder() 
				.code(401)
				.message("未授权(Unauthorized)")
				.build(),
				new ResponseMessageBuilder() 
				.code(403)
				.message("禁止访问(Forbidden)")
				.build(),
				new ResponseMessageBuilder() 
				.code(405)
				.message("参数格式错误(invalid input)")
				.build(),
				new ResponseMessageBuilder()   
				.code(500)
				.message("服务内部错误")
				.build());
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.useDefaultResponseMessages(false)
				.globalResponseMessage(RequestMethod.GET, responses)
				.globalResponseMessage(RequestMethod.POST, responses)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.minivision.authplat2.rest"))
				.paths(PathSelectors.any())
				.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("小视云端SDK授权服务")
				.description("小视云端SDK授权服务RESTful APIs")
				.contact(new Contact("ZhaoDeshan", null, "zhaodeshan@minivision.cn"))
				.version("1.0")
				.build();
	}

}
