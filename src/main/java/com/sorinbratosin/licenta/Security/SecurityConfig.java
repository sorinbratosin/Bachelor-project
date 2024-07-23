package com.sorinbratosin.licenta.Security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;
    private final ApiKeyFilter apiKeyFilter;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter, ApiKeyFilter apiKeyFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.apiKeyFilter = apiKeyFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/register", "/api/login").permitAll()
                        .requestMatchers("/postHumidity").hasAuthority("DEVICE")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class) // JWT filter este aplicat primul pentru utilizare generala
                .addFilterBefore(apiKeyFilter, UsernamePasswordAuthenticationFilter.class); // API Key filter este aplicat dupa pentru a nu interfera cu autentificarea

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
