package com.QuickStacker.back.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "formula_has_insumo")
@IdClass(FormulaHasInsumoId.class)
public class FormulaHasInsumo {
    @Id
    @ManyToOne
    @JoinColumn(name = "formula_id_formula", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore // Ignore formula to prevent circular reference
    private Formula formula;

    @Id
    @ManyToOne
    @JoinColumn(name = "insumo_id_insumo", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"formulaInsumos"}) // Prevent circular references
    private Insumo insumo;

    @Column(name = "quantidade_utilizada", nullable = false)
    private Integer quantidadeUtilizada;

    // Constructors
    public FormulaHasInsumo() {
    }

    public FormulaHasInsumo(Formula formula, Insumo insumo, Integer quantidadeUtilizada) {
        this.formula = formula;
        this.insumo = insumo;
        this.quantidadeUtilizada = quantidadeUtilizada;
    }

    // Getters and Setters
    public Formula getFormula() {
        return formula;
    }

    public void setFormula(Formula formula) {
        this.formula = formula;
    }

    public Insumo getInsumo() {
        return insumo;
    }

    public void setInsumo(Insumo insumo) {
        this.insumo = insumo;
    }

    public Integer getQuantidadeUtilizada() {
        return quantidadeUtilizada;
    }

    public void setQuantidadeUtilizada(Integer quantidadeUtilizada) {
        this.quantidadeUtilizada = quantidadeUtilizada;
    }
}

