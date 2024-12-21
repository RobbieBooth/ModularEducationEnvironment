package org.robbie.modulareducationenvironment;

import org.springframework.context.annotation.Configuration;
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
}
