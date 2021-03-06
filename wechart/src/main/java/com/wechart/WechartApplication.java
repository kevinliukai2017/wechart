package com.wechart;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@SpringBootApplication
@MapperScan("com.wechart.dao")
public class WechartApplication {

	
//	@Bean
//	public static PropertySourcesPlaceholderConfigurer propertyConfigure(){
//		return new PropertySourcesPlaceholderConfigurer();
//	}
	
	public static void main(String[] args) {
		SpringApplication.run(WechartApplication.class, args);
	}
}
