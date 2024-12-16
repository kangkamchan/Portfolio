package net.fullstack7.mooc.config;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import net.fullstack7.mooc.filter.AdminFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<Filter> AdminFilter() {
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();

        bean.setFilter(new AdminFilter());
//        bean.setOrder(2);
        bean.addUrlPatterns("/admin/*");

        return bean;
    }
}
