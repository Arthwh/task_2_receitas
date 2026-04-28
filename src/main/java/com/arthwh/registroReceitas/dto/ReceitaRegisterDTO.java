package com.arthwh.registroReceitas.dto;

import com.arthwh.registroReceitas.model.TipoReceitaEnum;

public record ReceitaRegisterDTO(
        String nome,
        String descricao,
        Double custo,
        TipoReceitaEnum tipoReceita
) {
}
