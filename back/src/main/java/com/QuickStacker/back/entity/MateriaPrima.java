package com.QuickStacker.back.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "materia_prima")
public class MateriaPrima {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_materia_prima")
    private Integer idMateriaPrima;

    @Column(name = "nome", length = 255, nullable = false)
    private String nome;

    @Column(name = "densidade")
    private Double densidade;

    @Column(name = "peso_unitario")
    private Double pesoUnitario;

    @OneToMany(mappedBy = "materiaPrima", cascade = CascadeType.ALL)
    private List<LoteMateriaPrima> lotesMateriaPrima;

    @OneToMany(mappedBy = "materiaPrima", cascade = CascadeType.ALL)
    private List<FormulaHasMateriaPrima> formulaMateriasPrimas;

    // Constructors
    public MateriaPrima() {
    }

    public MateriaPrima(String nome, Double densidade, Double pesoUnitario) {
        this.nome = nome;
        this.densidade = densidade;
        this.pesoUnitario = pesoUnitario;
    }

    // Getters and Setters
    public Integer getIdMateriaPrima() {
        return idMateriaPrima;
    }

    public void setIdMateriaPrima(Integer idMateriaPrima) {
        this.idMateriaPrima = idMateriaPrima;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getDensidade() {
        return densidade;
    }

    public void setDensidade(Double densidade) {
        this.densidade = densidade;
    }

    public Double getPesoUnitario() {
        return pesoUnitario;
    }

    public void setPesoUnitario(Double pesoUnitario) {
        this.pesoUnitario = pesoUnitario;
    }

    public List<LoteMateriaPrima> getLotesMateriaPrima() {
        return lotesMateriaPrima;
    }

    public void setLotesMateriaPrima(List<LoteMateriaPrima> lotesMateriaPrima) {
        this.lotesMateriaPrima = lotesMateriaPrima;
    }

    public List<FormulaHasMateriaPrima> getFormulaMateriasPrimas() {
        return formulaMateriasPrimas;
    }

    public void setFormulaMateriasPrimas(List<FormulaHasMateriaPrima> formulaMateriasPrimas) {
        this.formulaMateriasPrimas = formulaMateriasPrimas;
    }
}

