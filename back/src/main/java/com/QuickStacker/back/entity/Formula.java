package com.QuickStacker.back.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "formula")
public class Formula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_formula")
    private Integer idFormula;

    @Column(name = "descricao_modo_preparo", columnDefinition = "text")
    private String descricaoModoPreparo;

    @OneToOne
    @JoinColumn(name = "produto_id_produto", nullable = false, unique = true)
    @JsonIgnoreProperties({"formula", "lotes", "categoria"}) // Prevent circular references
    private Produto produto;

    @OneToMany(mappedBy = "formula", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"formula"}) // Prevent circular references
    private List<FormulaHasMateriaPrima> formulaMateriasPrimas;

    @OneToMany(mappedBy = "formula", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"formula"}) // Prevent circular references
    private List<FormulaHasInsumo> formulaInsumos;

    // Constructors
    public Formula() {
    }

    public Formula(String descricaoModoPreparo, Produto produto) {
        this.descricaoModoPreparo = descricaoModoPreparo;
        this.produto = produto;
    }

    // Getters and Setters
    public Integer getIdFormula() {
        return idFormula;
    }

    public void setIdFormula(Integer idFormula) {
        this.idFormula = idFormula;
    }

    public String getDescricaoModoPreparo() {
        return descricaoModoPreparo;
    }

    public void setDescricaoModoPreparo(String descricaoModoPreparo) {
        this.descricaoModoPreparo = descricaoModoPreparo;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public List<FormulaHasMateriaPrima> getFormulaMateriasPrimas() {
        return formulaMateriasPrimas;
    }

    public void setFormulaMateriasPrimas(List<FormulaHasMateriaPrima> formulaMateriasPrimas) {
        this.formulaMateriasPrimas = formulaMateriasPrimas;
    }

    public List<FormulaHasInsumo> getFormulaInsumos() {
        return formulaInsumos;
    }

    public void setFormulaInsumos(List<FormulaHasInsumo> formulaInsumos) {
        this.formulaInsumos = formulaInsumos;
    }
}

