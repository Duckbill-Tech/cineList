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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity // Habilita a configuração de segurança
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
                .csrf(csrf -> csrf.disable()) // Desativa CSRF para APIs REST stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Define sessões como stateless
                .authorizeHttpRequests(authorize -> authorize
                        // Permissões específicas para AuthController
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()

                        // Permissões para UsuarioController
                        .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll() // Criar usuário
                        .requestMatchers(HttpMethod.GET, "/api/usuarios").permitAll() // Listar usuários
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/user").hasAuthority("ROLE_USER") // Requer ROLE_USER

                        // Permissões para FilmeController
                        .requestMatchers(HttpMethod.GET, "/api/filmes").permitAll() // Listar filmes
                        .requestMatchers(HttpMethod.GET, "/api/filmes/{id}").permitAll() // Buscar filme por ID


                        // Permissões abertas para Swagger e documentação
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/swagger-resources/**", "/webjars/**").permitAll()

                        // Permissões para Reset Password TODO:
//                        .requestMatchers(HttpMethod.POST, "/auth/forgot-password").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/auth/reset-password").permitAll()
//                        .requestMatchers("/reset-password", "/forgot-password").permitAll()
//                        .requestMatchers("/error", "/error/**").permitAll()

                        // Qualquer outra requisição precisa de autenticação
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class) // Adiciona filtro de segurança
                .cors(cors -> cors.configurationSource(corsConfigurationSource())); // Configura CORS diretamente no filtro

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:5174"));// Permite a origem do frontend
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Métodos permitidos
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With")); // Permite os cabeçalhos
        configuration.setAllowCredentials(true); // Permite o envio de cookies e credenciais

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplica configuração a todas as rotas
        return source;
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