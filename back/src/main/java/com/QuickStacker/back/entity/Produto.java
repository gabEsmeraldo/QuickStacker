package com.QuickStacker.back.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "produto")
@JsonIgnoreProperties(ignoreUnknown = true) // Allow deserialization with partial data
public class Produto {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_produto")
  private Integer idProduto;

  @Column(name = "nome", length = 255, nullable = false)
  private String nome;

  @Column(name = "quantidade_total")
  private Integer quantidadeTotal;

  @Column(name = "validade_em_meses")
  private Integer validadeEmMeses;

  @ManyToOne
  @JoinColumn(name = "categoria_id_categoria", nullable = false)
  @JsonIgnoreProperties({ "produtos" }) // Prevent circular references
  private Categoria categoria;

  @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL)
  @JsonIgnoreProperties({ "produto" }) // Prevent circular references
  private List<Lote> lotes;

  @OneToOne(mappedBy = "produto", cascade = CascadeType.ALL)
  @JsonIgnoreProperties({ "produto" }) // Prevent circular references
  private Formula formula;

  // Constructors
  public Produto() {}

  public Produto(String nome, Integer validadeEmMeses, Categoria categoria) {
    this.nome = nome;
    this.validadeEmMeses = validadeEmMeses;
    this.categoria = categoria;
  }

  // Getters and Setters
  public Integer getIdProduto() {
    return idProduto;
  }

  public void setIdProduto(Integer idProduto) {
    this.idProduto = idProduto;
  }

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

  public Integer getQuantidadeTotal() {
    return quantidadeTotal;
  }

  public void setQuantidadeTotal(Integer quantidadeTotal) {
    this.quantidadeTotal = quantidadeTotal;
  }

  public Categoria getCategoria() {
    return categoria;
  }

  public void setCategoria(Categoria categoria) {
    this.categoria = categoria;
  }

  public List<Lote> getLotes() {
    return lotes;
  }

  public void setLotes(List<Lote> lotes) {
    this.lotes = lotes;
  }

  public Formula getFormula() {
    return formula;
  }

  public void setFormula(Formula formula) {
    this.formula = formula;
  }

  // metodo para calcular a quantidade total do produto usando a soma das quantidades dos lotes
  public void updateQuantidadeTotal() {
    this.quantidadeTotal = calculateQuantidadeTotal();
  }

  // metodo para calcular a quantidade total do produto usando a soma das quantidades dos lotes
  public Integer calculateQuantidadeTotal() {
    return lotes.stream().mapToInt(Lote::getQuantidade).sum();
  }
}
