package com.QuickStacker.back.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "lote_insumo")
public class LoteInsumo {
    @Id
    @Column(name = "id_lote_insumo", length = 50)
    private String idLoteInsumo;

    @Column(name = "data_chegada", nullable = false)
    private LocalDate dataChegada;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @Column(name = "custo_de_compra", nullable = false, columnDefinition = "numeric(12,4)")
    private Double custoDeCompra;

    @ManyToOne
    @JoinColumn(name = "id_insumo", nullable = false)
    private Insumo insumo;

    // Constructors
    public LoteInsumo() {
    }

    public LoteInsumo(String idLoteInsumo, LocalDate dataChegada, Integer quantidade, Double custoDeCompra, Insumo insumo) {
        this.idLoteInsumo = idLoteInsumo;
        this.dataChegada = dataChegada;
        this.quantidade = quantidade;
        this.custoDeCompra = custoDeCompra;
        this.insumo = insumo;
    }

    // Getters and Setters
    public String getIdLoteInsumo() {
        return idLoteInsumo;
    }

    public void setIdLoteInsumo(String idLoteInsumo) {
        this.idLoteInsumo = idLoteInsumo;
    }

    public LocalDate getDataChegada() {
        return dataChegada;
    }

    public void setDataChegada(LocalDate dataChegada) {
        this.dataChegada = dataChegada;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Double getCustoDeCompra() {
        return custoDeCompra;
    }

    public void setCustoDeCompra(Double custoDeCompra) {
        this.custoDeCompra = custoDeCompra;
    }

    public Insumo getInsumo() {
        return insumo;
    }

    public void setInsumo(Insumo insumo) {
        this.insumo = insumo;
    }
}

