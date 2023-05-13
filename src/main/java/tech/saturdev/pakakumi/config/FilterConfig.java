package tech.saturdev.pakakumi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tech.saturdev.pakakumi.repository.RequestRepository;
import tech.saturdev.pakakumi.util.RequestFilter;

@Configuration
public class FilterConfig {

    @Autowired
    private RequestRepository requestRepository;

    @Bean
    public FilterRegistrationBean<RequestFilter> requestFilter() {
        FilterRegistrationBean<RequestFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestFilter(requestRepository));
        registrationBean.addUrlPatterns("/*"); // Specify the URL pattern that you want to intercept
        return registrationBean;
    }
}
