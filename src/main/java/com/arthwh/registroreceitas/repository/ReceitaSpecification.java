package com.arthwh.registroreceitas.repository;

import com.arthwh.registroreceitas.model.Receita;
import com.arthwh.registroreceitas.model.TipoReceitaEnum;
import java.time.LocalDate;
import org.springframework.data.jpa.domain.Specification;

public class ReceitaSpecification {
  private ReceitaSpecification() {
    /* This utility class should not be instantiated */
  }

  public static Specification<Receita> hasRecipeType(TipoReceitaEnum tipoReceita) {
    if (tipoReceita == null) {
      return null;
    } // Retorna um predicado vazio
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.equal(root.get("tipoReceita"), tipoReceita);
  }

  public static Specification<Receita> hasInitialDate(LocalDate dataInicio) {
    if (dataInicio == null) {
      return null;
    } // Retorna um predicado vazio
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.greaterThanOrEqualTo(
            root.get("dataRegistro").as(LocalDate.class), dataInicio);
  }
}
