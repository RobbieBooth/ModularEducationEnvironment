package org.robbie.modulareducationenvironment.authentication;

import org.robbie.modulareducationenvironment.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //JWT secret key
    @Value("${app.jwt.verifier.key}")
    private String SECRET;

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(SECRET);
    http
            .csrf(csrf -> csrf.disable())
//            .cors(cors -> cors.disable())
            .authorizeHttpRequests(auth ->
//                    auth.requestMatchers("/settings").hasRole("user") // Spring will check for "ROLE_EDUCATOR"
                    auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Allow preflight OPTION
                            .requestMatchers("/error").permitAll()
                                    .anyRequest().authenticated())
            .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .anonymous(anonymous -> anonymous.disable()) // Disable anonymous authentication because its causing true jwt to get over ridden!!!
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);


    return http.build();
}
}


