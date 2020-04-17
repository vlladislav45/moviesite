package com.filmi3k.movies.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().disable()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers ("/","/login", "/register").permitAll()
                .antMatchers("/styles/**", "/images/**", "/movies/**").permitAll()
                .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/login");
    }
}
