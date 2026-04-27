package com.arthwh.registroReceitas.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "receita")
public class Receita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",  updatable = false, nullable = false)
    private int id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @CreationTimestamp
    @Column(name = "data_registro", nullable = false)
    private Timestamp dataRegistro;

    @Column(name = "custo", nullable = false)
    private double custo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_receita", nullable = false, columnDefinition = "tipo_receita_paladar")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private TipoReceitaEnum tipoReceita;
}
