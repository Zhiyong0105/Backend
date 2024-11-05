package org.springframe.backend.config;

import org.springframe.backend.constants.SecurityConst;
import org.springframe.backend.filter.JwtAuthorizeFilter;
import org.springframe.backend.handler.SecurityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    @Autowired
    private SecurityHandler securityHandler;
    @Autowired
    private JwtAuthorizeFilter jwtAuthorizeFilter;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(SecurityConst.AUTH_CHECK_ARRAY).authenticated()
                        .anyRequest().permitAll()
                )
                .formLogin(from -> from
                        .loginProcessingUrl(SecurityConst.LOGIN)
                        .successHandler(securityHandler::onAuthenticationSuccess)
                        .failureHandler(securityHandler::onAuthenticationFailure)


                )
                .logout( from -> from
                        .logoutUrl(SecurityConst.LOGOUT)
                        .logoutSuccessHandler(securityHandler::onLogoutSuccess)


                )
                .exceptionHandling(from -> from
                        .authenticationEntryPoint(securityHandler::onUnAuthenticated)
                        .accessDeniedHandler(securityHandler::onAccessDeny)
                )
                .addFilterBefore(jwtAuthorizeFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }



}
