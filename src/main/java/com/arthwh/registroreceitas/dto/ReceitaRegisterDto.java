package com.arthwh.registroreceitas.dto;

import com.arthwh.registroreceitas.model.TipoReceitaEnum;
import java.math.BigDecimal;

public record ReceitaRegisterDto(
    String nome, String descricao, BigDecimal custo, TipoReceitaEnum tipoReceita) {}
