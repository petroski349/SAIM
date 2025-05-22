package com.example.saim.Controllers.Security.Filters;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

public class FilterConfiguration {
    @Bean
    public FilterRegistrationBean<AccessFilter> registrationBean(){
        // registrando o filtro
        FilterRegistrationBean<AccessFilter> register = new FilterRegistrationBean<>();
        register.setFilter(new AccessFilter());
        // definindo as URLs para aplicar o filtro
        register.addUrlPatterns("/api/*");
        return register;
    }
}
