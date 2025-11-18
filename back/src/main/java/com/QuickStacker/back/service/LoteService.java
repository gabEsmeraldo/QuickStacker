package com.QuickStacker.back.service;

import com.QuickStacker.back.entity.Lote;
import com.QuickStacker.back.entity.Produto;
import com.QuickStacker.back.repository.LoteRepository;
import com.QuickStacker.back.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LoteService {

    @Autowired
    private LoteRepository repository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<Lote> findAll() {
        return repository.findAll();
    }

    public Optional<Lote> findById(Integer id) {
        return repository.findById(id);
    }

    public List<Lote> findByProdutoId(Integer produtoId) {
        return repository.findByProdutoIdProduto(produtoId);
    }

    public List<Lote> findExpired() {
        return repository.findByDataValidadeBefore(LocalDate.now());
    }

    public List<Lote> findExpiringBetween(LocalDate start, LocalDate end) {
        return repository.findByDataValidadeBetween(start, end);
    }

    public Lote create(Lote lote) {
        // Validate produto exists
        if (lote.getProduto() != null && lote.getProduto().getIdProduto() != null) {
            Produto produto = produtoRepository.findById(lote.getProduto().getIdProduto())
                .orElseThrow(() -> new IllegalArgumentException("Produto not found with id: " + lote.getProduto().getIdProduto()));
            lote.setProduto(produto);
        }
        
        Lote saved = repository.save(lote);
        
        // Update produto quantidade total
        if (saved.getProduto() != null) {
            saved.getProduto().updateQuantidadeTotal();
            produtoRepository.save(saved.getProduto());
        }
        
        return saved;
    }

    public Lote update(Integer id, Lote lote) {
        Lote existing = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Lote not found with id: " + id));
        
        existing.setDataValidade(lote.getDataValidade());
        existing.setDataProducao(lote.getDataProducao());
        existing.setCustoUnitarioProducao(lote.getCustoUnitarioProducao());
        existing.setQuantidade(lote.getQuantidade());
        
        // Update produto if provided
        if (lote.getProduto() != null && lote.getProduto().getIdProduto() != null) {
            Produto produto = produtoRepository.findById(lote.getProduto().getIdProduto())
                .orElseThrow(() -> new IllegalArgumentException("Produto not found with id: " + lote.getProduto().getIdProduto()));
            existing.setProduto(produto);
        }
        
        Lote updated = repository.save(existing);
        
        // Update produto quantidade total
        if (updated.getProduto() != null) {
            updated.getProduto().updateQuantidadeTotal();
            produtoRepository.save(updated.getProduto());
        }
        
        return updated;
    }

    public void delete(Integer id) {
        Lote lote = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Lote not found with id: " + id));
        
        Produto produto = lote.getProduto();
        
        repository.deleteById(id);
        
        // Update produto quantidade total after deletion
        if (produto != null) {
            produto.updateQuantidadeTotal();
            produtoRepository.save(produto);
        }
    }
}

