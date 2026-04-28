package com.arthwh.registroReceitas.repository;

import com.arthwh.registroReceitas.model.Receita;
import com.arthwh.registroReceitas.model.TipoReceitaEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
// O ActiveProfiles puxa o application-test.properties
@ActiveProfiles("test")
// Essa anotação impede o Spring de usar o H2 e força o uso do Postgres configurado
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReceitaSpecificationTest {

    @Autowired
    private ReceitaRepository receitaRepository;

    @BeforeEach
    void setUp() {
        // Limpamos o banco antes de cada teste
        receitaRepository.deleteAll();

        // Criamos receitas para o nosso cenário
        Receita r1 = new Receita();
        r1.setNome("Bolo de Chocolate");
        r1.setDescricao("Bolo simples");
        r1.setCusto(20.0);
        r1.setTipoReceita(TipoReceitaEnum.DOCE); // Supondo que DOCE exista
        r1.setDataRegistro(Timestamp.from(Instant.now()));
        receitaRepository.save(r1);

        Receita r2 = new Receita();
        r2.setNome("Macarrão");
        r2.setDescricao("Massa alho e oleo");
        r2.setCusto(10.0);
        r2.setTipoReceita(TipoReceitaEnum.SALGADA); // Supondo que SALGADA exista
        // Colocando uma data antiga (ex: 2023-01-01)
        r2.setDataRegistro(Timestamp.valueOf("2023-01-01 10:00:00"));
        receitaRepository.save(r2);
    }

    @Test
    void deveBuscarPorTipoReceitaCorretamente() {
        // Ação: Buscamos apenas receitas SALGADAS
        Specification<Receita> spec = ReceitaSpecification.hasRecipeType(TipoReceitaEnum.SALGADA);
        List<Receita> resultado = receitaRepository.findAll(spec);

        // Verificação: Deve voltar apenas 1 receita (o Macarrão)
        assertEquals(1, resultado.size());
        assertEquals("Macarrão", resultado.get(0).getNome());
    }

    @Test
    void deveBuscarPorDataInicialCorretamente() {
        // Ação: Buscar receitas registradas a partir de hoje
        LocalDate hoje = LocalDate.now();
        Specification<Receita> spec = ReceitaSpecification.hasInitialDate(hoje);
        List<Receita> resultado = receitaRepository.findAll(spec);

        // Verificação: O macarrão é antigo, então deve voltar só o Bolo (1 resultado)
        assertEquals(1, resultado.size());
        assertEquals("Bolo de Chocolate", resultado.get(0).getNome());
    }
}