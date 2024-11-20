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
    private final CustomUserDetailsService userDetailsService;
    @Autowired
    private final SecurityFilter securityFilter;

    public SecurityConfig(CustomUserDetailsService userDetailsService, SecurityFilter securityFilter) {
        this.userDetailsService = userDetailsService;
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desativa CSRF para APIs stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Define sessões como stateless
                .authorizeHttpRequests(authorize -> authorize
                        // Permissões específicas para AuthController
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()

                        // Permissões para UsuarioController
                        .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll() // Criar usuário
                        .requestMatchers(HttpMethod.GET, "/api/usuarios").authenticated() // Listar usuários
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/{id}").authenticated() // Buscar por ID
                        .requestMatchers(HttpMethod.PUT, "/api/usuarios/{id}").authenticated() // Atualizar por ID
                        .requestMatchers(HttpMethod.DELETE, "/api/usuarios/{id}").authenticated() // Deletar logicamente
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/user").hasAuthority("ROLE_USER") // Requer ROLE_USER

                        // Permissões para FilmeController
                        .requestMatchers(HttpMethod.POST, "/api/filmes").authenticated() // Criar filme
                        .requestMatchers(HttpMethod.GET, "/api/filmes").authenticated() // Listar filmes
                        .requestMatchers(HttpMethod.GET, "/api/filmes/{id}").authenticated() // Buscar filme por ID
                        .requestMatchers(HttpMethod.PUT, "/api/filmes/{id}").authenticated() // Atualizar filme por ID
                        .requestMatchers(HttpMethod.DELETE, "/api/filmes/{id}").authenticated() // Deletar logicamente

                        // Permissões abertas para Swagger e documentação
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/swagger-resources/**", "/webjars/**").permitAll()

                        // Qualquer outra requisição precisa de autenticação
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
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