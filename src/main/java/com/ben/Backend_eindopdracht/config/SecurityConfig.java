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
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        // registratie nieuwe user:
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                        .requestMatchers(HttpMethod.GET, "/auth").permitAll()

                        // ─── Users
                        .requestMatchers(HttpMethod.GET, "/users/**").permitAll()
                        /*.hasAnyRole("ADMIN", "COMPLIANCE_OFFICER")*/

                        .requestMatchers(HttpMethod.PUT, "/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")

                        // ─── SecurityRoles (toegangsbeheer)
                        /*.requestMatchers("/securityroles/**").hasRole("ADMIN")*/
                        .requestMatchers("/securityroles/**").permitAll()


                        // ─── KYC (inzage/valideren)
                        .requestMatchers("/kycfiles/**")
                        .hasAnyRole("COMPLIANCE_OFFICER", "ADMIN")

                        // ─── Wallets
                        // Traders en Admins mogen wallets aanmaken/bewerken/verwijderen
                        .requestMatchers(HttpMethod.POST,   "/wallets/**")
                        .hasAnyRole("TRADER","ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/wallets/**")
                        .hasAnyRole("TRADER","ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/wallets/**")
                        .hasAnyRole("TRADER","ADMIN")
                        // Inzage in wallets: ook CO
                        .requestMatchers(HttpMethod.GET,    "/wallets/**")
                        .hasAnyRole("TRADER","COMPLIANCE_OFFICER","ADMIN")

                        // ─── Orders
                        // Traders en Admins mogen orders beheren; inzage ook voor CO
                        .requestMatchers(HttpMethod.POST,   "/orders/**")
                        .hasAnyRole("TRADER","ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/orders/**")
                        .hasAnyRole("TRADER","ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/orders/**")
                        .hasAnyRole("TRADER","ADMIN")
                        .requestMatchers(HttpMethod.GET,    "/orders/**")
                        .hasAnyRole("TRADER","COMPLIANCE_OFFICER","ADMIN")

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
