package org.springframe.backend.config;

import jakarta.annotation.Resource;
import org.springframe.backend.constants.SecurityConst;
import org.springframe.backend.filter.JwtAuthorizeFilter;
import org.springframe.backend.handler.SecurityHandler;
import org.springframe.backend.service.impl.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    @Resource
    private SecurityHandler securityHandler;
    @Resource
    private JwtAuthorizeFilter jwtAuthorizeFilter;

    @Resource
    private CustomOAuth2UserService customOAuth2UserService;

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
                .oauth2Login(oauth ->oauth
//                        .loginPage(SecurityConst.GITHUB_LOGIN)
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler(securityHandler::onGithubAuthenticationSuccess)
                )


                .exceptionHandling(from -> from
                        .authenticationEntryPoint(securityHandler::onUnAuthenticated)
                        .accessDeniedHandler(securityHandler::onAccessDeny)
                )
                .cors(cors ->cors.configurationSource(corsConfigurationSource()))
                .addFilterBefore(jwtAuthorizeFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:5173");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return source;

    }



}
