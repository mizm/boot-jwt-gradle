package com.ildong.erpmonitor.api.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	private static final String[] EXCLUDE_PATHS = {
			"/error/**",
			"/api/login/",
			"/api/**",
			"/h2-console",
			"/static/**", 
			"/swagger-ui.html", 
			"/webjars/**", 
			"/v2/api-docs", 
			"/configuration/security/**", 
			"/configuration/ui", 
			"/swagger-resources/**"

			};

	@Autowired
	private JwtInterceptor jwtInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(jwtInterceptor)
						.addPathPatterns("/**")
						.excludePathPatterns(EXCLUDE_PATHS);
	}

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
        		.allowedOrigins("*")
        		.allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "HEAD", "OPTIONS")
        		.allowedHeaders("*")
        		.allowCredentials(true).maxAge(3600);
;
    }
}