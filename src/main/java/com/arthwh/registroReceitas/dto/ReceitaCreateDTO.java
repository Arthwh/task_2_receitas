package com.arthwh.registroReceitas.dto;

import com.arthwh.registroReceitas.model.TipoReceitaEnum;

public record ReceitaCreateDTO(
        String nome,
        String descricao,
        Double custo,
        TipoReceitaEnum tipoReceita
) {
}
