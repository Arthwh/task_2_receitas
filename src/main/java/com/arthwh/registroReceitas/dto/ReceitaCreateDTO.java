package com.arthwh.registroReceitas.dto;

import com.arthwh.registroReceitas.model.Receita;
import com.arthwh.registroReceitas.model.TipoReceitaEnum;

public record ReceitaCreateDTO(
        String nome,
        String descricao,
        Double custo,
        TipoReceitaEnum tipoReceita
) {
    public ReceitaCreateDTO(Receita receita){
        this(
                receita.getNome(),
                receita.getDescricao(),
                receita.getCusto(),
                receita.getTipoReceita()
        );
    }
}
