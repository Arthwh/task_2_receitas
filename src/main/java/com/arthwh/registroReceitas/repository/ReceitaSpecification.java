package com.arthwh.registroReceitas.repository;

import com.arthwh.registroReceitas.model.Receita;
import com.arthwh.registroReceitas.model.TipoReceitaEnum;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ReceitaSpecification {
    public static Specification<Receita> hasRecipeType(TipoReceitaEnum tipoReceita) {
        if (tipoReceita == null) return null; // Retorna um predicado vazio
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("tipoReceita"), tipoReceita);
    }

    public static Specification<Receita> hasInitialDate(LocalDate dataInicio) {
        if (dataInicio == null) return null;// Retorna um predicado vazio
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root
                        .get("dataRegistro").as(LocalDate.class), dataInicio);
    }
}
