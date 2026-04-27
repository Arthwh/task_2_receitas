package com.arthwh.registroReceitas.dto;

public record ReceitaUpdateDTO(
        Integer id,
        String descricao,
        Double custo
) {
}
