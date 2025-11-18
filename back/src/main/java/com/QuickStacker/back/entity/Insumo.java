package com.QuickStacker.back.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "insumo")
public class Insumo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_insumo")
    private Integer idInsumo;

    @Column(name = "nome", length = 255, nullable = false)
    private String nome;

    @Column(name = "custo_unitario", columnDefinition = "numeric(12,4)")
    private Double custoUnitario;

    @OneToMany(mappedBy = "insumo", cascade = CascadeType.ALL)
    private List<LoteInsumo> lotesInsumo;

    @OneToMany(mappedBy = "insumo", cascade = CascadeType.ALL)
    private List<FormulaHasInsumo> formulaInsumos;

    // Constructors
    public Insumo() {
    }

    public Insumo(String nome) {
        this.nome = nome;
    }

    // Getters and Setters
    public Integer getIdInsumo() {
        return idInsumo;
    }

    public void setIdInsumo(Integer idInsumo) {
        this.idInsumo = idInsumo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getCustoUnitario() {
        return custoUnitario;
    }

    public void setCustoUnitario(Double custoUnitario) {
        this.custoUnitario = custoUnitario;
    }

    public List<LoteInsumo> getLotesInsumo() {
        return lotesInsumo;
    }

    public void setLotesInsumo(List<LoteInsumo> lotesInsumo) {
        this.lotesInsumo = lotesInsumo;
    }

    public List<FormulaHasInsumo> getFormulaInsumos() {
        return formulaInsumos;
    }

    public void setFormulaInsumos(List<FormulaHasInsumo> formulaInsumos) {
        this.formulaInsumos = formulaInsumos;
    }

    // Helper method to calculate CustoUnitario from the latest LoteInsumo
    // This can be used to update the custo_unitario field
    @Transient
    public Double calculateCustoUnitario() {
        if (lotesInsumo == null || lotesInsumo.isEmpty()) {
            return null;
        }
        // Get the most recent batch
        LoteInsumo latestLote = lotesInsumo.stream()
            .max((l1, l2) -> l1.getDataChegada().compareTo(l2.getDataChegada()))
            .orElse(null);
        
        if (latestLote != null && latestLote.getQuantidade() > 0) {
            return (double) latestLote.getCustoDeCompra() / latestLote.getQuantidade();
        }
        return null;
    }
}

