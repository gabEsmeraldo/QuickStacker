package com.QuickStacker.back.entity;

import java.io.Serializable;
import java.util.Objects;

public class FormulaHasInsumoId implements Serializable {
    private Integer formula;
    private Integer insumo;

    // Constructors
    public FormulaHasInsumoId() {
    }

    public FormulaHasInsumoId(Integer formula, Integer insumo) {
        this.formula = formula;
        this.insumo = insumo;
    }

    // Getters and Setters
    public Integer getFormula() {
        return formula;
    }

    public void setFormula(Integer formula) {
        this.formula = formula;
    }

    public Integer getInsumo() {
        return insumo;
    }

    public void setInsumo(Integer insumo) {
        this.insumo = insumo;
    }

    // Equals and HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FormulaHasInsumoId that = (FormulaHasInsumoId) o;
        return Objects.equals(formula, that.formula) &&
               Objects.equals(insumo, that.insumo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(formula, insumo);
    }
}

