package com.dongnv.employee_evaluation_system.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(
                                "/department/**",
                                "/user",
                                "/user/create",
                                "/user/deactivate/*",
                                "/user/activate/*",
                                "/user/set-role/*",
                                "/user/set-password/*",
                                "/user/delete/*")
                        .hasRole("ADMIN")
                        .requestMatchers("/department", "/employee/**", "/evaluation/**")
                        .hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.GET, "/employee", "/evaluation", "/")
                        .hasAnyRole("ADMIN", "MANAGER", "CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/images/*", "/user/login", "/user/register")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/user/register")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .formLogin(formLogin -> formLogin
                        .loginPage("/user/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", true)
                        .failureHandler((request, response, exception) -> {
                            if (exception instanceof DisabledException
                                    || exception instanceof InternalAuthenticationServiceException) {
                                response.sendRedirect("/user/login?inactive=true");
                            } else {
                                response.sendRedirect("/user/login?error=true");
                            }
                        }))
                .logout(logout -> logout.logoutUrl("/logout").deleteCookies("JSESSIONID"))
                .exceptionHandling(
                        exception -> exception.accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.sendRedirect("/error/access-denied");
                        }))
                .csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder(10));
    }
}
