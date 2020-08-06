package com.filmi3k.movies.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.filmi3k.movies.services.base.UserService;
import com.filmi3k.movies.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    private final ObjectMapper mapper;

    @Autowired
    public JwtRequestFilter(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = httpServletRequest.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);

            try {
                boolean isExpired = jwtTokenUtil.isTokenExpired(jwt);
            } catch (ExpiredJwtException expired) {
                httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value()); // error 401
                httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
                mapper.writeValue(httpServletResponse.getWriter(), Map.of("error", "Token is expired"));
                return;
            } catch (Exception e) {
                System.out.println("Catch exception=========================\n");
                e.printStackTrace();
                return;
            }


            username = jwtTokenUtil.extractUsername(jwt);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userService.loadUserByUsername(username);

                //Check if the user is not found in the database but token is still there
                //Against front-end manipulating
                if(userDetails == null) {
                    httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value()); // error 401
                    httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    mapper.writeValue(httpServletResponse.getWriter(), Map.of("error", "Token is not expired, but the user is not found"));
                }

                if (jwtTokenUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
                            null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
