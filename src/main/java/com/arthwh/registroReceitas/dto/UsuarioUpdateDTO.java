package com.arthwh.registroReceitas.dto;

import com.arthwh.registroReceitas.model.Usuario;

public record UsuarioUpdateDTO(
        Integer id,
        String nome,
        String senha
) {
    public UsuarioUpdateDTO(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getNome(),
                usuario.getSenha()
        );
    }
}
