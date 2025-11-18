package com.QuickStacker.back.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FormulaHasMateriaPrimaId implements Serializable {
    @Column(name = "formula_id_formula", nullable = false)
    private Integer formulaIdFormula;

    @Column(name = "materia_prima_id_materia_prima", nullable = false)
    private Integer materiaPrimaIdMateriaPrima;

    // Constructors
    public FormulaHasMateriaPrimaId() {
    }

    public FormulaHasMateriaPrimaId(Integer formulaIdFormula, Integer materiaPrimaIdMateriaPrima) {
        this.formulaIdFormula = formulaIdFormula;
        this.materiaPrimaIdMateriaPrima = materiaPrimaIdMateriaPrima;
    }

    // Getters and Setters
    public Integer getFormulaIdFormula() {
        return formulaIdFormula;
    }

    public void setFormulaIdFormula(Integer formulaIdFormula) {
        this.formulaIdFormula = formulaIdFormula;
    }

    public Integer getMateriaPrimaIdMateriaPrima() {
        return materiaPrimaIdMateriaPrima;
    }

    public void setMateriaPrimaIdMateriaPrima(Integer materiaPrimaIdMateriaPrima) {
        this.materiaPrimaIdMateriaPrima = materiaPrimaIdMateriaPrima;
    }

    // Equals and HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FormulaHasMateriaPrimaId that = (FormulaHasMateriaPrimaId) o;
        return Objects.equals(formulaIdFormula, that.formulaIdFormula) &&
               Objects.equals(materiaPrimaIdMateriaPrima, that.materiaPrimaIdMateriaPrima);
    }

    @Override
    public int hashCode() {
        return Objects.hash(formulaIdFormula, materiaPrimaIdMateriaPrima);
    }
}

