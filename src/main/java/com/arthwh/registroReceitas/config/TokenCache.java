package com.arthwh.registroReceitas.config;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenCache {
    // Mapa que guarda <Token, LoginDoUsuario>
    private final Map<String, String> tokensAtivos = new ConcurrentHashMap<>();

    public void salvarToken(String token, String login) {
        tokensAtivos.put(token, login);
    }

    public String getLoginPorToken(String token) {
        return tokensAtivos.get(token);
    }
}
