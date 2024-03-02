package org.masterleonardo.usersapi.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.masterleonardo.usersapi.dto.UserDTO;
import org.masterleonardo.usersapi.security.PersonDetails;
import org.masterleonardo.usersapi.services.UsersService;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private UsersService usersService;
    private Environment environment;


    public AuthenticationFilter(AuthenticationManager authenticationManager, UsersService usersService, Environment environment) {
        this.usersService = usersService;
        this.environment = environment;
        this.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try{
            UserDTO creds = new ObjectMapper().readValue(request.getInputStream(), UserDTO.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getLogin(),
                            creds.getPassword(),
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_BASE"))
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        String curUserUsername = ((PersonDetails) authResult.getPrincipal()).getUsername();
        org.masterleonardo.usersapi.models.User curUser = usersService.loadUserByLogin(curUserUsername);
        Date createdAt = new Date();
        String token = JWT.create().
                        withSubject("UserDetails").
                        withClaim("username", curUser.getLogin()).
                        withClaim("role", curUser.getRole()).
                        withIssuedAt(createdAt).
                        withIssuer(environment.getProperty("spring.application.name")).
                        withExpiresAt(new Date(createdAt.getTime() + environment.getProperty("jwt.token.duration", Long.class))).
                sign(Algorithm.HMAC256(environment.getProperty("jwt.secretkey")));
        response.addHeader("Authorization", token);



    }
}
