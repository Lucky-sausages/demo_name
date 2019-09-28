package com.example.demoname;

import com.example.demoname.interceptor.AuthorizationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class DemonameApplication implements WebMvcConfigurer {

	private AuthorizationInterceptor authorizationInterceptor;

	@Autowired
	public void setApplicationInterceptor(AuthorizationInterceptor authorizationInterceptor) {
		this.authorizationInterceptor = authorizationInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authorizationInterceptor);
	}
	public static void main(String[] args) {
		SpringApplication.run(DemonameApplication.class, args);
	}

}
