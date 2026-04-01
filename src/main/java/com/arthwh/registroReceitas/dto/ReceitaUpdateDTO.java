package com.arthwh.registroReceitas.dto;

import com.arthwh.registroReceitas.model.Receita;

public record ReceitaUpdateDTO(
        Integer id,
        String descricao,
        Double custo
) {
    public ReceitaUpdateDTO(Receita receita){
        this(
                receita.getId(),
                receita.getDescricao(),
                receita.getCusto()
        );
    }
}
