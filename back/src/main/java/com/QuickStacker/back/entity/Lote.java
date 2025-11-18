package com.QuickStacker.back.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "lote")
public class Lote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lote")
    private Integer idLote;

    @Column(name = "data_validade", nullable = false)
    private LocalDate dataValidade;

    @Column(name = "data_producao", nullable = false)
    private LocalDate dataProducao;

    @Column(name = "custo_unitario_producao", columnDefinition = "numeric(12,4)")
    private Double custoUnitarioProducao;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @ManyToOne
    @JoinColumn(name = "id_produto", nullable = false)
    private Produto produto;

    // Constructors
    public Lote() {
    }

    public Lote(LocalDate dataValidade, LocalDate dataProducao, Double custoUnitarioProducao, Integer quantidade, Produto produto) {
        this.dataValidade = dataValidade;
        this.dataProducao = dataProducao;
        this.custoUnitarioProducao = custoUnitarioProducao;
        this.quantidade = quantidade;
        this.produto = produto;
    }

    // Getters and Setters
    public Integer getIdLote() {
        return idLote;
    }

    public void setIdLote(Integer idLote) {
        this.idLote = idLote;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }

    public LocalDate getDataProducao() {
        return dataProducao;
    }

    public void setDataProducao(LocalDate dataProducao) {
        this.dataProducao = dataProducao;
    }

    public Double getCustoUnitarioProducao() {
        return custoUnitarioProducao;
    }

    public void setCustoUnitarioProducao(Double custoUnitarioProducao) {
        this.custoUnitarioProducao = custoUnitarioProducao;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }
}

