package com.arthwh.registroReceitas.dto;

public record LoginDTO(
        String login,
        String senha
) {
    public  LoginDTO(String login, String senha) {
        this.login = login;
        this.senha = senha;
    }
}
