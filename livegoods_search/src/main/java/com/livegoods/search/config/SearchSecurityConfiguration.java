package com.livegoods.search.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SearchSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /**
         * 使用默认配置super.configure(http)，增加特定配置，做登录验证
         */
        http.authorizeRequests()
                .antMatchers("/init").authenticated()
                .antMatchers("/**").permitAll();
        super.configure(http);
    }
}
