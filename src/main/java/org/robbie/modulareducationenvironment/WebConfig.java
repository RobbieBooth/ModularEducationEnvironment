package org.robbie.modulareducationenvironment;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map /views/** to the "view" directory in the module
        registry.addResourceHandler("/views/**")
                .addResourceLocations("classpath:/view/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:5173") // React app running on localhost:3000
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed methods
                .allowedHeaders("Content-Type", "Authorization") // Allowed headers
                .allowCredentials(true); // If you're using cookies or credentials;
    }
}
