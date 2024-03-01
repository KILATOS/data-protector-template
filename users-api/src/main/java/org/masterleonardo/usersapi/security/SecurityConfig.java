package org.masterleonardo.usersapi.security;

import org.masterleonardo.usersapi.security.filters.AuthenticationFilter;
import org.masterleonardo.usersapi.security.filters.CustomIpFilter;
import org.masterleonardo.usersapi.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UsersService usersService;
    private final Environment environment;
    private static final String[] AUTH_WHITELIST = {
            "/authenticate",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/api/v1/app/user/auth/**",
            "/v3/api-docs/**"
    };
    @Autowired
    public SecurityConfig(BCryptPasswordEncoder passwordEncoder, UsersService usersService, Environment environment) {
        this.passwordEncoder = passwordEncoder;
        this.usersService = usersService;
        this.environment = environment;
    }
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(usersService).passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManager authManager = authManager(http);
        http.csrf().disable()
                .authenticationManager(authManager)
                .addFilterBefore(new CustomIpFilter(), BasicAuthenticationFilter.class)
                .addFilter(new AuthenticationFilter(authManager, usersService, environment))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .requestMatchers(new RegexRequestMatcher("/login.*", "GET")).permitAll()
                        .requestMatchers(HttpMethod.POST,"/user").permitAll()
                        .anyRequest().authenticated()
                ).headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer.
                        loginProcessingUrl("/login").
                        permitAll().
                        defaultSuccessUrl("/test"));
        return http.build();
    }






}
