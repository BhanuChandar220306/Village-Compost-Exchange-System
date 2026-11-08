
package com.village.compost.config;

import com.village.compost.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // SECURITY FILTER CHAIN
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {})
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                .authorizeHttpRequests(auth -> auth

                        // PUBLIC APIs
                        .requestMatchers(
                                "/api/users/register",
                                "/api/users/login"
                        ).permitAll()

                        // ADMIN DASHBOARD
                        .requestMatchers(
                                "/api/dashboard/admin"
                        ).hasAuthority("ADMIN")

                        // COLLECTOR APIs
                        .requestMatchers(
                                "/api/waste/pending",
                                "/api/waste/*/accept",
                                "/api/waste/*/collect",
                                "/api/pickup/**"
                        ).hasAuthority("COLLECTOR")

                        // FARMER COMPOST REQUEST APIs
                        .requestMatchers(
                                "/api/compost/request",
                                "/api/compost/my"
                        ).hasAuthority("FARMER")

                        // ADMIN COMPOST MANAGEMENT
                        .requestMatchers(
                                "/api/compost/all",
                                "/api/compost/*/approve",
                                "/api/compost/*/reject"
                        ).hasAuthority("ADMIN")

                        // ADMIN INVENTORY MANAGEMENT
                        .requestMatchers(
                                "/api/inventory/**"
                        ).hasAuthority("ADMIN")

                        // ALLOCATION
                        .requestMatchers(
                                "/api/allocation/**"
                        ).authenticated()

                        // REWARDS
                        .requestMatchers(
                                "/api/rewards/**"
                        ).authenticated()

                        // NOTIFICATIONS
                        .requestMatchers(
                                "/api/notifications/**"
                        ).hasAnyAuthority(
                                "HOUSEHOLD",
                                "COLLECTOR",
                                "FARMER",
                                "ADMIN"
                        )

                        // LOGGED-IN USER DASHBOARD
                        .requestMatchers(
                                "/api/dashboard/my"
                        ).authenticated()

                        // WASTE APIs
                        .requestMatchers(
                                "/api/waste/**"
                        ).authenticated()

                        // EVERYTHING ELSE
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    // CORS CONFIGURATION
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        // Allowed frontend origins
        configuration.setAllowedOrigins(List.of(
                "http://localhost:5500",
                "http://127.0.0.1:5500",
                "http://192.168.1.100:5500",
                "http://192.168.1.101:5500",
                "http://192.168.1.102:5500",
                "http://192.168.1.103:5500",
                "http://172.25.33.75:5500",
                "http://172.25.58.237:5500",
                "http://192.168.1.104:5500"
        ));

        // Allowed HTTP methods
        configuration.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "OPTIONS"
        ));

        // Allowed headers
        configuration.setAllowedHeaders(List.of("*"));

        // Allow credentials
        configuration.setAllowCredentials(true);

        // Apply configuration to all endpoints
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }

    // PASSWORD ENCODER
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
