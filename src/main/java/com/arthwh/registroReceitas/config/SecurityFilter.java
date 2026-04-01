package com.arthwh.registroReceitas.config;

import com.arthwh.registroReceitas.repository.UsuarioRepository;
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

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    private TokenCache tokenCache;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var token = recuperarToken(request);

        System.out.println("TOKEN RECEBIDO: " + token);

        if (token != null) {
            var login = tokenCache.getLoginPorToken(token);
            System.out.println("LOGIN ENCONTRADO NO CACHE: " + login);
            if (login != null) {
                var usuario = usuarioRepository.getReferenceByLogin(login);

                if (usuario != null) {
                    var authorities = java.util.List.of(new SimpleGrantedAuthority("ROLE_USER"));
                    var authentication = new UsernamePasswordAuthenticationToken(
                            usuario, null, authorities
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("DEBUG: Usuário autenticado? " + SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return authorizationHeader; // Retorna o que vier se não tiver Bearer
    }
}
