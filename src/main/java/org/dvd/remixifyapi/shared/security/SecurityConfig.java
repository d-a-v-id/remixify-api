package org.dvd.remixifyapi.shared.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@Profile("prod")
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())  // Enable CORS
            .csrf(csrf -> csrf.disable())      // Disable CSRF for REST API
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, "/api/recipes/**").permitAll()  // Public GET endpoints
                .requestMatchers("/api/auth/**").permitAll()                     // Public auth endpoints
                .anyRequest().authenticated()
            )
            // Disable frame options to prevent clickjacking
            .headers(headers -> headers
                .frameOptions(frame -> frame.deny())
            )
            // Optionally enforce HTTPS (if you have an SSL certificate configured)
            .requiresChannel(channel -> channel
                .anyRequest().requiresSecure()
            );
        
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
