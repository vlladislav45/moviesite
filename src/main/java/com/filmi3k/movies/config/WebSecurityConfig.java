package com.filmi3k.movies.config;

import com.filmi3k.movies.services.base.UserService;
import com.filmi3k.movies.utils.JSONparser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
                .cors()
            .and()
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers ("/login", "/register", "/", "/index", "/stream/mp4/Kenpachi").permitAll()
                .antMatchers("/styles/**", "/posters/**", "/movies/**", "anime/**").permitAll()
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()
            .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/index")
                .successHandler(getSuccessHandler())
            .and()
                .rememberMe()
                .rememberMeParameter("rememberMe")
                .key("remember")
                .userDetailsService(this.userService)
                .tokenValiditySeconds(10)
            .and()
                .logout()
                .invalidateHttpSession(true)
                .deleteCookies("remember")
                .permitAll()
            .and()
                .exceptionHandling()
                .accessDeniedPage("/unauthorized");
    }

    private AuthenticationSuccessHandler getSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
//                User user = userService.getUserByUsername(((Principal)authentication.getPrincipal()).getName());
                httpServletResponse.getWriter().write(JSONparser.toJson(authentication.getPrincipal()));
            }
        };
    }
}