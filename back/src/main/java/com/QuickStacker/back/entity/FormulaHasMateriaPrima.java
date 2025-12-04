package com.QuickStacker.back.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "formula_has_materia_prima")
public class FormulaHasMateriaPrima {

  @EmbeddedId
  private FormulaHasMateriaPrimaId id;

  @ManyToOne
  @MapsId("formulaIdFormula")
  @JoinColumn(name = "formula_id_formula", nullable = false)
  @com.fasterxml.jackson.annotation.JsonIgnore // Ignore formula to prevent circular reference
  private Formula formula;

  @ManyToOne
  @MapsId("materiaPrimaIdMateriaPrima")
  @JoinColumn(name = "materia_prima_id_materia_prima", nullable = false)
  @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"formulaMateriasPrimas", "lotesMateriaPrima", "formula"}) // Prevent circular references
  private MateriaPrima materiaPrima;

  @Column(name = "quantidade_utilizada", nullable = false)
  private Double quantidadeUtilizada;

  @Column(name = "custo_por_quantidade", columnDefinition = "numeric(12,4)")
  private Double custoPorQuantidade;

  // Constructors
  public FormulaHasMateriaPrima() {
    this.id = new FormulaHasMateriaPrimaId();
  }

  public FormulaHasMateriaPrima(
    Formula formula,
    MateriaPrima materiaPrima,
    Double quantidadeUtilizada
  ) {
    this.id = new FormulaHasMateriaPrimaId(
      formula.getIdFormula(),
      materiaPrima.getIdMateriaPrima()
    );
    this.formula = formula;
    this.materiaPrima = materiaPrima;
    this.quantidadeUtilizada = quantidadeUtilizada;
  }

  // Getters and Setters
  public FormulaHasMateriaPrimaId getId() {
    return id;
  }

  public void setId(FormulaHasMateriaPrimaId id) {
    this.id = id;
  }

  public Formula getFormula() {
    return formula;
  }

  public void setFormula(Formula formula) {
    this.formula = formula;
  }

  public MateriaPrima getMateriaPrima() {
    return materiaPrima;
  }

  public void setMateriaPrima(MateriaPrima materiaPrima) {
    this.materiaPrima = materiaPrima;
  }

  public Double getQuantidadeUtilizada() {
    return quantidadeUtilizada;
  }

  public void setQuantidadeUtilizada(Double quantidadeUtilizada) {
    this.quantidadeUtilizada = quantidadeUtilizada;
  }

  public Double getCustoPorQuantidade() {
    return custoPorQuantidade;
  }

  public void setCustoPorQuantidade(Double custoPorQuantidade) {
    this.custoPorQuantidade = custoPorQuantidade;
  }

  // Helper method to calculate CustoPorQuantidade from CustoCompra * QuantidadeUtilizada
  // This can be used to update the custo_por_quantidade field
  @Transient
  public Double calculateCustoPorQuantidade() {
    if (materiaPrima == null || quantidadeUtilizada == null) {
      return null;
    }

    List<LoteMateriaPrima> lotes = materiaPrima.getLotesMateriaPrima();
    if (lotes == null || lotes.isEmpty()) {
      return null;
    }

    // Get the most recent batch
    LoteMateriaPrima latestLote = lotes
      .stream()
      .max((l1, l2) -> l1.getDataChegada().compareTo(l2.getDataChegada()))
      .orElse(null);

    if (
      latestLote != null &&
      latestLote.getCustoDeCompra() != null &&
      latestLote.getQuantidadeUnidades() > 0
    ) {
      // Calculate cost per unit from the batch
      Double custoUnitario =
        latestLote.getCustoDeCompra() / latestLote.getQuantidadeUnidades();
      return custoUnitario * quantidadeUtilizada;
    }
    return null;
  }
}
