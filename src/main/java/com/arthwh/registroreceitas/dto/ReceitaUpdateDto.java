package com.arthwh.registroreceitas.dto;

import java.math.BigDecimal;

public record ReceitaUpdateDto(Integer id, String descricao, BigDecimal custo) {}
