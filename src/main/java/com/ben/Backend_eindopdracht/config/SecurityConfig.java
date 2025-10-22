package com.ben.Backend_eindopdracht.config;


import com.ben.Backend_eindopdracht.security.JwtRequestFilter;
import com.ben.Backend_eindopdracht.security.JwtService;
import com.ben.Backend_eindopdracht.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(UserService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtService jwtService) throws Exception {

        http

                .httpBasic(b -> b.disable()) // zet Basic uit als je JWT gaat gebruiken
                .authorizeHttpRequests(auth -> auth
                        // ─── Publiek (geen auth)
                        .requestMatchers(
                                "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/api-docs/**",
                                "/actuator/health", "/actuator/info",
                                "/login"              // jouw login endpoint (JWT)
                        ).permitAll()

                        // registration
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()

                        //view registration
                        .requestMatchers(HttpMethod.GET, "/auth").hasRole("ADMIN")

                        // ─── Users
                        //Only admin can view all users
                        .requestMatchers(HttpMethod.GET, "/users/**")
                        .hasAnyRole("ADMIN")
                        //Only admin can update all users
                        .requestMatchers(HttpMethod.PUT, "/users/**").hasRole("ADMIN")
                        //Only admin can delete all users
                        .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")

                        // ─── SecurityRoles (toegangsbeheer)
                        // Only admin can create and update  securityroles
                        .requestMatchers("/securityroles/**").hasRole("ADMIN")


                        // ─── KYC (inzage/valideren)
                        // All can upload KYC documents
                        .requestMatchers(HttpMethod.POST, "/kycfiles/*/upload").permitAll()
                        // Only CO can view all kyc requests
                        .requestMatchers(HttpMethod.GET, "/kycfiles/**").hasRole("COMPLIANCE_OFFICER")
                        // Only CO can update status and kyc request
                        .requestMatchers(HttpMethod.PUT, "/kycfiles/**").hasRole("COMPLIANCE_OFFICER")
                        // Admin and CO can delete kyc requests
                        .requestMatchers(HttpMethod.DELETE, "/kycfiles/**").hasAnyRole("COMPLIANCE_OFFICER", "ADMIN" )


                        // ─── Wallets
                        // Traders en Admins can create wallets
                        .requestMatchers(HttpMethod.POST,   "/wallets/**")
                        .hasAnyRole("TRADER","ADMIN")
                        // Admins can update wallets
                        .requestMatchers(HttpMethod.PUT,    "/wallets/**")
                        .hasRole("ADMIN")
                        // Traders en Admins can delete wallets
                        .requestMatchers(HttpMethod.DELETE, "/wallets/**")
                        .hasAnyRole("ADMIN","TRADER")
                        // Admins can view all
                        .requestMatchers(HttpMethod.GET,    "/wallets/**")
                        .hasRole("ADMIN")

                        // ─── Orders
                        // Traders en Admins can create orders
                        .requestMatchers(HttpMethod.POST,   "/orders/**")
                        .hasAnyRole("TRADER","ADMIN")
                        // Admins can update orders
                        .requestMatchers(HttpMethod.PUT,    "/orders/**")
                        .hasRole("ADMIN")
                        // Admins can delete orders
                        .requestMatchers(HttpMethod.DELETE, "/orders/**")
                        .hasAnyRole("ADMIN","TRADER")
                        // Admins can view all orders
                        .requestMatchers(HttpMethod.GET,    "/orders/**")
                        .hasRole("ADMIN")

                        // ─── Overig: ingelogd
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtRequestFilter(jwtService), UsernamePasswordAuthenticationFilter.class)
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {
                })
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        ;

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        var builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);

        return builder.build();
    }
}
