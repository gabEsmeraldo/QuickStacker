package com.QuickStacker.back.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class FormulaHasMateriaPrimaDTO {

  @NotNull(message = "Formula ID is required")
  private Integer formulaId;

  @NotNull(message = "Materia Prima ID is required")
  private Integer materiaPrimaId;

  @NotNull(message = "Quantidade utilizada is required")
  @Positive(message = "Quantidade utilizada must be positive")
  private Double quantidadeUtilizada;

  // Constructors
  public FormulaHasMateriaPrimaDTO() {}

  public FormulaHasMateriaPrimaDTO(
    Integer formulaId,
    Integer materiaPrimaId,
    Double quantidadeUtilizada
  ) {
    this.formulaId = formulaId;
    this.materiaPrimaId = materiaPrimaId;
    this.quantidadeUtilizada = quantidadeUtilizada;
  }

  // Getters and Setters
  public Integer getFormulaId() {
    return formulaId;
  }

  public void setFormulaId(Integer formulaId) {
    this.formulaId = formulaId;
  }

  public Integer getMateriaPrimaId() {
    return materiaPrimaId;
  }

  public void setMateriaPrimaId(Integer materiaPrimaId) {
    this.materiaPrimaId = materiaPrimaId;
  }

  public Double getQuantidadeUtilizada() {
    return quantidadeUtilizada;
  }

  public void setQuantidadeUtilizada(Double quantidadeUtilizada) {
    this.quantidadeUtilizada = quantidadeUtilizada;
  }

  @Override
  public String toString() {
    return (
      "FormulaHasMateriaPrimaDTO{" +
      "formulaId=" +
      formulaId +
      ", materiaPrimaId=" +
      materiaPrimaId +
      ", quantidadeUtilizada=" +
      quantidadeUtilizada +
      '}'
    );
  }
}
