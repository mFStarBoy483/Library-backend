package com.luv2code.spring_boot_library.config;

import com.okta.spring.boot.oauth.Okta;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // Disable Cross-Site Request Forgery (CSRF)
        http.csrf(csrf -> csrf.disable());

        // Define endpoint-specific access rules
        http.authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                        // Permit access to public endpoints (if any)
                        .requestMatchers("/api/public/**").permitAll()
                        // Require authentication for all other secure endpoints
                        .requestMatchers("/api/reviews/secure/**",
                                "/api/books/secure/**",
                                "/api/messages/secure/**",
                                "/api/admin/secure/**").authenticated()
                        .anyRequest().permitAll()
        );

        // Enable JWT authentication using Okta
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt());

        // Global CORS configuration
        http.cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration corsConfig = new CorsConfiguration();
            corsConfig.setAllowedOrigins(List.of("https://localhost:3000")); // Allow this origin
            corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Allow these methods
            corsConfig.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type")); // Allow essential headers
            corsConfig.setAllowCredentials(true); // Enable credentials if needed
            return corsConfig;
        }));

        // Friendly 401 response body (for unauthenticated access)
        Okta.configureResourceServer401ResponseBody(http);

        return http.build();
    }
}
