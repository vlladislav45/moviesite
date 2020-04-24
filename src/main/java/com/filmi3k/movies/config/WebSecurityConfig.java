package com.filmi3k.movies.config;

import com.filmi3k.movies.services.base.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserService userService;

    @Autowired
    public WebSecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().disable()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers ("/login", "/register").permitAll()
                .antMatchers("/styles/**", "/images/**", "/movies/**").permitAll()
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()
            .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/index")
            .and()
                .logout()
                .invalidateHttpSession(true)
                .permitAll()
            .and()
                .rememberMe()
                .rememberMeParameter("rememberMe")
                .key("PLYOK")
                .userDetailsService(this.userService)
                .rememberMeCookieName("KLYOK")
                .tokenValiditySeconds(60)
            .and()
                .exceptionHandling()
                .accessDeniedPage("/unauthorized");
    }
}