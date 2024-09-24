package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.kata.spring.boot_security.demo.service.StringToRoleSetConverter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private StringToRoleSetConverter stringToRoleSetConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringToRoleSetConverter);
    }
}