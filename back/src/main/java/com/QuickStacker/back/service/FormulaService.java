package com.QuickStacker.back.service;

import com.QuickStacker.back.entity.Formula;
import com.QuickStacker.back.entity.Produto;
import com.QuickStacker.back.repository.FormulaRepository;
import com.QuickStacker.back.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FormulaService {

    @Autowired
    private FormulaRepository repository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<Formula> findAll() {
        return repository.findAll();
    }

    public Optional<Formula> findById(Integer id) {
        return repository.findById(id);
    }

    public Optional<Formula> findByProdutoId(Integer produtoId) {
        return repository.findByProdutoIdProduto(produtoId);
    }

    public Formula create(Formula formula) {
        // Validate produto is required
        if (formula.getProduto() == null || formula.getProduto().getIdProduto() == null) {
            throw new IllegalArgumentException("Produto is required and must have an idProduto");
        }
        
        // Validate produto exists
        Produto produto = produtoRepository.findById(formula.getProduto().getIdProduto())
            .orElseThrow(() -> new IllegalArgumentException("Produto not found with id: " + formula.getProduto().getIdProduto()));
        
        // Check if this produto already has a formula (OneToOne relationship)
        Optional<Formula> existingFormula = repository.findByProdutoIdProduto(produto.getIdProduto());
        if (existingFormula.isPresent()) {
            throw new IllegalArgumentException("Produto with id " + produto.getIdProduto() + " already has a formula");
        }
        
        formula.setProduto(produto);
        return repository.save(formula);
    }

    public Formula update(Integer id, Formula formula) {
        Formula existing = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Formula not found with id: " + id));
        
        existing.setDescricaoModoPreparo(formula.getDescricaoModoPreparo());
        
        // Update produto if provided
        if (formula.getProduto() != null && formula.getProduto().getIdProduto() != null) {
            Produto produto = produtoRepository.findById(formula.getProduto().getIdProduto())
                .orElseThrow(() -> new IllegalArgumentException("Produto not found with id: " + formula.getProduto().getIdProduto()));
            existing.setProduto(produto);
        }
        
        return repository.save(existing);
    }

    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Formula not found with id: " + id);
        }
        repository.deleteById(id);
    }
}

