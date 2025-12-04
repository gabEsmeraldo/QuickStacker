package com.QuickStacker.back.dto;

import jakarta.validation.constraints.NotNull;

public class ProdutoDTO {

  @NotNull(message = "Nome is required")
  private String nome;

  private Integer validadeEmMeses;

  @NotNull(message = "Categoria ID is required")
  private Integer categoriaId;

  // Constructors
  public ProdutoDTO() {}

  public ProdutoDTO(String nome, Integer validadeEmMeses, Integer categoriaId) {
    this.nome = nome;
    this.validadeEmMeses = validadeEmMeses;
    this.categoriaId = categoriaId;
  }

  // Getters and Setters
  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public Integer getValidadeEmMeses() {
    return validadeEmMeses;
  }

  public void setValidadeEmMeses(Integer validadeEmMeses) {
    this.validadeEmMeses = validadeEmMeses;
  }

  public Integer getCategoriaId() {
    return categoriaId;
  }

  public void setCategoriaId(Integer categoriaId) {
    this.categoriaId = categoriaId;
  }
}

