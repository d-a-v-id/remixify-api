package org.dvd.remixifyapi.shared.security;

import java.util.Arrays;

import org.dvd.remixifyapi.shared.security.jwt.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@Configuration
@Profile("prod")
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(
                        auth ->
                                auth.requestMatchers("/api/auth/**").permitAll()
                                    // Public endpoints
                                    .requestMatchers(HttpMethod.GET, "/api/recipes/**").permitAll()
                                    .requestMatchers(HttpMethod.GET, "/api/users/**").permitAll()
                                    .requestMatchers(HttpMethod.GET, "/api/ingredients/**").permitAll()
                                    // Protected endpoints
                                    .requestMatchers(HttpMethod.POST, "/api/recipes/**").authenticated()
                                    .requestMatchers(HttpMethod.PUT, "/api/recipes/**").authenticated()
                                    .requestMatchers(HttpMethod.DELETE, "/api/recipes/**").authenticated()
                                    .requestMatchers(HttpMethod.POST, "/api/users/**").authenticated()
                                    .requestMatchers(HttpMethod.PUT, "/api/users/**").authenticated()
                                    .requestMatchers(HttpMethod.DELETE, "/api/users/**").authenticated()
                                    .anyRequest().authenticated())
                .headers(headers -> headers.frameOptions(frame -> frame.deny()))
                .requiresChannel(channel -> channel.anyRequest().requiresSecure())
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("https://remixify.netlify.app"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "Accept",
            "Origin",
            "X-Requested-With",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers"
        ));
        configuration.setExposedHeaders(Arrays.asList(
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials"
        ));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
