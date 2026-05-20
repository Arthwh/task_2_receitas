package com.arthwh.registroReceitas.dto;

import java.math.BigDecimal;

public record ReceitaUpdateDTO(
        Integer id,
        String descricao,
        BigDecimal custo
) {
}
