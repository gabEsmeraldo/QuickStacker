package com.QuickStacker.back.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "lote_materia_prima")
public class LoteMateriaPrima {
    @Id
    @Column(name = "id_lote_mp", length = 50)
    private String idLoteMP;

    @Column(name = "data_validade")
    private LocalDate dataValidade;

    @Column(name = "data_chegada", nullable = false)
    private LocalDate dataChegada;

    @Column(name = "custo_de_compra", nullable = false, columnDefinition = "numeric(12,4)")
    private Double custoDeCompra;

    @Column(name = "quantidade_unidades", nullable = false)
    private Double quantidadeUnidades;

    @Column(name = "fornecedor", length = 255)
    private String fornecedor;

    @Column(name = "numero_nota_fiscal")
    private Integer numeroNotaFiscal;

    @Column(name = "quantidade_de_caixas")
    private Integer quantidadeDeCaixas;

    @Column(name = "laudo")
    private Boolean laudo;

    @Column(name = "responsavel_recebimento", length = 255)
    private String responsavelRecebimento;

    @ManyToOne
    @JoinColumn(name = "id_materia_prima", nullable = false)
    private MateriaPrima materiaPrima;

    // Constructors
    public LoteMateriaPrima() {
    }

    public LoteMateriaPrima(String idLoteMP, LocalDate dataValidade, LocalDate dataChegada, Double custoDeCompra, 
                           Double quantidadeUnidades, String fornecedor, Integer numeroNotaFiscal, 
                           Integer quantidadeDeCaixas, Boolean laudo, String responsavelRecebimento, MateriaPrima materiaPrima) {
        this.idLoteMP = idLoteMP;
        this.dataValidade = dataValidade;
        this.dataChegada = dataChegada;
        this.custoDeCompra = custoDeCompra;
        this.quantidadeUnidades = quantidadeUnidades;
        this.fornecedor = fornecedor;
        this.numeroNotaFiscal = numeroNotaFiscal;
        this.quantidadeDeCaixas = quantidadeDeCaixas;
        this.laudo = laudo;
        this.responsavelRecebimento = responsavelRecebimento;
        this.materiaPrima = materiaPrima;
    }

    // Getters and Setters
    public String getIdLoteMP() {
        return idLoteMP;
    }

    public void setIdLoteMP(String idLoteMP) {
        this.idLoteMP = idLoteMP;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }

    public LocalDate getDataChegada() {
        return dataChegada;
    }

    public void setDataChegada(LocalDate dataChegada) {
        this.dataChegada = dataChegada;
    }

    public Double getCustoDeCompra() {
        return custoDeCompra;
    }

    public void setCustoDeCompra(Double custoDeCompra) {
        this.custoDeCompra = custoDeCompra;
    }

    public Double getQuantidadeUnidades() {
        return quantidadeUnidades;
    }

    public void setQuantidadeUnidades(Double quantidadeUnidades) {
        this.quantidadeUnidades = quantidadeUnidades;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Integer getNumeroNotaFiscal() {
        return numeroNotaFiscal;
    }

    public void setNumeroNotaFiscal(Integer numeroNotaFiscal) {
        this.numeroNotaFiscal = numeroNotaFiscal;
    }

    public Integer getQuantidadeDeCaixas() {
        return quantidadeDeCaixas;
    }

    public void setQuantidadeDeCaixas(Integer quantidadeDeCaixas) {
        this.quantidadeDeCaixas = quantidadeDeCaixas;
    }

    public Boolean getLaudo() {
        return laudo;
    }

    public void setLaudo(Boolean laudo) {
        this.laudo = laudo;
    }

    public MateriaPrima getMateriaPrima() {
        return materiaPrima;
    }

    public void setMateriaPrima(MateriaPrima materiaPrima) {
        this.materiaPrima = materiaPrima;
    }

    public String getResponsavelRecebimento() {
        return responsavelRecebimento;
    }

    public void setResponsavelRecebimento(String responsavelRecebimento) {
        this.responsavelRecebimento = responsavelRecebimento;
    }
}

