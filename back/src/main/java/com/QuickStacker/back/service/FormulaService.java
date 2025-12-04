package com.QuickStacker.back.service;

import com.QuickStacker.back.dto.FormulaDTO;
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

    public Formula createFromDTO(FormulaDTO dto) {
        System.out.println("[FormulaService] createFromDTO called with produtoId=" + (dto != null ? dto.getProdutoId() : "null"));
        // Validate produtoId is required
        if (dto == null || dto.getProdutoId() == null) {
            System.out.println("[FormulaService] ERROR: produtoId is null!");
            throw new IllegalArgumentException("Produto ID is required");
        }
        
        System.out.println("[FormulaService] Looking up produto with id=" + dto.getProdutoId());
        // Validate produto exists
        Produto produto = produtoRepository.findById(dto.getProdutoId())
            .orElseThrow(() -> {
                System.out.println("[FormulaService] ERROR: Produto not found with id=" + dto.getProdutoId());
                return new IllegalArgumentException("Produto not found with id: " + dto.getProdutoId());
            });
        
        System.out.println("[FormulaService] Produto found: " + produto.getNome());
        
        // Check if this produto already has a formula (OneToOne relationship)
        Optional<Formula> existingFormula = repository.findByProdutoIdProduto(produto.getIdProduto());
        if (existingFormula.isPresent()) {
            System.out.println("[FormulaService] ERROR: Produto already has a formula");
            throw new IllegalArgumentException("Produto with id " + produto.getIdProduto() + " already has a formula");
        }
        
        System.out.println("[FormulaService] Creating new formula");
        Formula formula = new Formula();
        formula.setDescricaoModoPreparo(dto.getDescricaoModoPreparo());
        formula.setProduto(produto);
        Formula saved = repository.save(formula);
        System.out.println("[FormulaService] Formula saved with id=" + saved.getIdFormula());
        return saved;
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

    public Formula updateFromDTO(Integer id, FormulaDTO dto) {
        Formula existing = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Formula not found with id: " + id));
        
        existing.setDescricaoModoPreparo(dto.getDescricaoModoPreparo());
        
        // Update produto if provided
        if (dto.getProdutoId() != null) {
            Produto produto = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new IllegalArgumentException("Produto not found with id: " + dto.getProdutoId()));
            
            // Check if changing produto, and if new produto already has a formula
            if (!existing.getProduto().getIdProduto().equals(produto.getIdProduto())) {
                Optional<Formula> existingFormula = repository.findByProdutoIdProduto(produto.getIdProduto());
                if (existingFormula.isPresent() && !existingFormula.get().getIdFormula().equals(id)) {
                    throw new IllegalArgumentException("Produto with id " + produto.getIdProduto() + " already has a formula");
                }
            }
            
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

