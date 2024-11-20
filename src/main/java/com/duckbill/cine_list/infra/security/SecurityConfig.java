package com.duckbill.cine_list.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // Classe de configuração
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desativa o CSRF (não é necessário em APIs stateless)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Define a política de sessão como stateless
                .authorizeHttpRequests(authorize -> authorize
                        // Endpoints abertos
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/swagger-resources/**", "/webjars/**").permitAll() // Swagger
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll() // Login
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll() // Registro
                        .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll() // Criação de usuários
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/user").hasAuthority("ROLE_USER") // Acesso ao "/user" apenas com ROLE_USER

                        // Permissões para /api/filmes
                        .requestMatchers(HttpMethod.POST, "/api/filmes").authenticated() // Criação de filmes
                        .requestMatchers(HttpMethod.GET, "/api/filmes").authenticated() // Listar todos os filmes
                        .requestMatchers(HttpMethod.GET, "/api/filmes/**").authenticated() // Buscar filme por ID
                        .requestMatchers(HttpMethod.PUT, "/api/filmes/**").authenticated() // Atualizar filme por ID
                        .requestMatchers(HttpMethod.DELETE, "/api/filmes/**").authenticated() // Excluir filme por ID

                        // Demais requisições
                        .anyRequest().authenticated() // Todas as outras requisições precisam de autenticação
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class); // Adiciona o filtro de segurança
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Define o BCrypt como algoritmo de hashing para senhas
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager(); // Obtém o gerenciador de autenticação do Spring
    }
}