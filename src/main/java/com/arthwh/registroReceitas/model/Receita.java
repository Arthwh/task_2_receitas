package com.arthwh.registroReceitas.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;

import java.sql.Timestamp;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Timestamp getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(Timestamp dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public double getCusto() {
        return custo;
    }

    public void setCusto(double custo) {
        this.custo = custo;
    }

    public TipoReceitaEnum getTipoReceita() {
        return tipoReceita;
    }

    public void setTipoReceita(TipoReceitaEnum tipoReceita) {
        this.tipoReceita = tipoReceita;
    }
}
