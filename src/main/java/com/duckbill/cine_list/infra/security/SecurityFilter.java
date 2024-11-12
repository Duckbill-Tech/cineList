package com.duckbill.cine_list.infra.security;

import com.duckbill.cine_list.db.entity.Usuario;
import com.duckbill.cine_list.db.repository.UsuarioRepository;
import com.duckbill.cine_list.infra.security.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Recupera o token da requisição
        String token = this.recoverToken(request);

        // Valida o token e obtém o email do usuário
        String email = tokenService.validateToken(token);

        if (email != null) {
            // Busca o usuário pelo email -> caso não encontrar o usuário:
            Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            // Cria uma lista com a autoridade padrão
            var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

            // Cria o token de autenticação para o Spring Security contendo as roles
            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, authorities);

            // Define o contexto de autenticação para o usuário
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Continua a cadeia de filtros
        filterChain.doFilter(request, response);
    }

    // Metodo para extrair o token JWT do cabeçalho Authorization
    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }
}