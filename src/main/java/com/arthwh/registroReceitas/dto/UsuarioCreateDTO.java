package com.arthwh.registroReceitas.dto;

import com.arthwh.registroReceitas.model.Usuario;

public record UsuarioCreateDTO(
        String nome,
        String login,
        String senha
) {
    public UsuarioCreateDTO(Usuario usuario){
        this(
                usuario.getNome(),
                usuario.getLogin(),
                usuario.getSenha()
        );
    }
}
