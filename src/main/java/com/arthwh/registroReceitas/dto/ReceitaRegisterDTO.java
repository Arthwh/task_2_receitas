package com.arthwh.registroReceitas.dto;

import com.arthwh.registroReceitas.model.TipoReceitaEnum;

import java.math.BigDecimal;

public record ReceitaRegisterDTO(
        String nome,
        String descricao,
        BigDecimal custo,
        TipoReceitaEnum tipoReceita
) {
}
