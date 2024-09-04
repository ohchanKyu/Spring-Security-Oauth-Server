package com.example.SpringSecurityOauthServer.config;

import com.example.SpringSecurityOauthServer.config.principal.PrincipalOauthDetailsService;
import com.example.SpringSecurityOauthServer.exception.ErrorEncodedString;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final PrincipalOauthDetailsService principalOauthDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        String loginErrorMessage = URLEncoder.encode(ErrorEncodedString.INVALID_CREDENTIALS.getMessage(), StandardCharsets.UTF_8);

        http
                .csrf(AbstractHttpConfigurer::disable)
                .headers((headerConfig) ->
                        headerConfig.frameOptions(
                                HeadersConfigurer.FrameOptionsConfig::disable
                        )
                )
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers("/auth/**","/public/**",
                                "/css/**","/js/**").permitAll()
                                .requestMatchers("/api/user/**").authenticated()
                                .requestMatchers("/api/manager/**").hasAnyRole("ADMIN", "MANAGER")
                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .exceptionHandling((exceptionConfig) ->
                        exceptionConfig
                                .accessDeniedHandler((request, response, accessDeniedException) -> {
                                    response.sendRedirect("/public/except");
                                })
                )
                .formLogin((formLogin) ->
                        formLogin
                                .loginPage("/public/login")
                                .loginProcessingUrl("/auth/sign-in")
                                .usernameParameter("userId")
                                .passwordParameter("password")
                                .defaultSuccessUrl("/public/main",true)
                                .failureUrl("/public/login?error=true&errorMessage="+loginErrorMessage)
                                .permitAll()
                )
                .logout((logoutConfig) ->
                        logoutConfig
                            .logoutUrl("/auth/logout")
                            .deleteCookies("JSESSIONID")
                            .invalidateHttpSession(true)
                            .logoutSuccessUrl("/public/main")
                )
                .oauth2Login((oauthLogin) ->
                        oauthLogin
                                .loginPage("/public/login")
                                .userInfoEndpoint(userInfoEndpointConfig ->
                                        userInfoEndpointConfig.userService(principalOauthDetailsService))
                                .failureUrl("/public/login?error=true&errorMessage="+loginErrorMessage)
                                .defaultSuccessUrl("/public/main",true)
                                .permitAll()
                );
        return http.build();
    }
}
