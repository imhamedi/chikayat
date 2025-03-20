package com.reclamations.chikayat.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> {
                })
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
                    auth.requestMatchers(
                            "/api/utilisateurs/login",
                            "/api/utilisateurs/register",
                            "/api/utilisateurs/forgot-password",
                            "/api/utilisateurs/update-profile",
                            "/api/utilisateurs/profile",
                            "/api/niveaux",
                            "/api/utilisateurs/reset-password",
                            "/api/utilisateurs/password-rules",
                            "/api/reclamations/generate-num-inscription",
                            "/api/reclamations/territoires/nom_commune_langue_2",
                            "/api/reclamations",
                            "/api/reclamations/**",
                            "/api/ocr",
                            "/api/ocr/extract-text",
                            "/api/reclamations/by-identifiant/**",
                            "/api/niveaux",
                            "/api/niveaux/**",
                            "/api/source-reclamation",
                            "/api/source-reclamation/**",
                            "/api/territoires",
                            "/api/type-destinataire",
                            "/api/type-destinataire/**",
                            "/api/type-reclamation",
                            "/api/type-requete",
                            "/api/voie-reponse",
                            "/api/source-reclamation",
                            "/api/type-reclamation/**",
                            "/api/territoires/**",
                            "/api/type-requete/**",
                            "/api/type-destinataire",
                            "/api/type-reclamation",
                            "/api/type-requete",
                            "/api/reclamations//release-num-inscription",
                            "/api/voie-reponse",
                            "/api/reclamations/export",
                            "/api/reclamations/type-requete/nom_requete_langue_2",
                            "/api/reclamations/type-reclamation/nom_reclamation_langue_2",
                            "/api/reclamations/type-destinataire/nom_destinataire_langue_2",
                            "/api/reclamations/source-reclamation/nom_source_langue_2",
                            "/api/reclamations/voie-reponse/nom_voie_langue_2",
                            "/api/reclamations/utilisateurs/login",
                            "/api/voie-reponse/",
                            "/api/voie-reponse/**",
                            "/api/reclamations/**",
                            "/api/reclamations/*/**",
                            "/api/admin/users",
                            "/api/reclamations/*/retourAutorite",
                            "/api/reclamations/stats/duration/7days",
                            "/api/reclamations/stats/duration/30days",
                            "/api/reclamations/stats/duration/year",
                            "/retourAutorite/details",
                            "/retourAutorite/details/**",
                            "/api/reclamations/cloture/search",
                            "/api/reclamations/ouverture/search",
                            "/api/reclamations/cloture/*",
                            "/api/reclamations/ouverture/*",
                            "/api/reclamations/stats/duration/**")
                            .permitAll();
                    auth.requestMatchers("/api/admin/**", "/api/admin/delete-user/**", "/api/admin/create-user",
                            "/api/niveau/**", "/api/source-reclamation/**", "/api/voie-reponse/**",
                            "/api/admin/users/**")
                            .hasAuthority("ADMIN");
                    auth.requestMatchers("/api/reclamations/**", "/api/source-reclamation/**",
                            "/api/reclamations/*/retourAutorite")
                            .hasAnyAuthority("ADMIN", "UTILISATEUR");
                    auth.requestMatchers("/api/utilisateurs/update-profile", "/api/utilisateurs/**")
                            .authenticated();
                    auth.anyRequest().authenticated();
                })
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
