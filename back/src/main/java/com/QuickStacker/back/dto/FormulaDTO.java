package com.QuickStacker.back.dto;

import jakarta.validation.constraints.NotNull;

public class FormulaDTO {

  private String descricaoModoPreparo;

  @NotNull(message = "Produto ID is required")
  private Integer produtoId;

  // Constructors
  public FormulaDTO() {}

  public FormulaDTO(String descricaoModoPreparo, Integer produtoId) {
    this.descricaoModoPreparo = descricaoModoPreparo;
    this.produtoId = produtoId;
  }

  // Getters and Setters
  public String getDescricaoModoPreparo() {
    return descricaoModoPreparo;
  }

  public void setDescricaoModoPreparo(String descricaoModoPreparo) {
    this.descricaoModoPreparo = descricaoModoPreparo;
  }

  public Integer getProdutoId() {
    return produtoId;
  }

  public void setProdutoId(Integer produtoId) {
    this.produtoId = produtoId;
  }
}

