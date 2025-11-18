package com.QuickStacker.back.service;

import com.QuickStacker.back.entity.Categoria;
import com.QuickStacker.back.entity.Produto;
import com.QuickStacker.back.repository.CategoriaRepository;
import com.QuickStacker.back.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Produto> findAll() {
        return repository.findAll();
    }

    public Optional<Produto> findById(Integer id) {
        return repository.findById(id);
    }

    public Optional<Produto> findByNome(String nome) {
        return repository.findByNome(nome);
    }

    public List<Produto> findByCategoriaId(Integer categoriaId) {
        return repository.findByCategoriaIdCategoria(categoriaId);
    }

    public Produto create(Produto produto) {
        // Validate categoria exists
        if (produto.getCategoria() != null && produto.getCategoria().getIdCategoria() != null) {
            Categoria categoria = categoriaRepository.findById(produto.getCategoria().getIdCategoria())
                .orElseThrow(() -> new IllegalArgumentException("Categoria not found with id: " + produto.getCategoria().getIdCategoria()));
            produto.setCategoria(categoria);
        }
        
        // Update quantidade total from lotes if available
        if (produto.getLotes() != null && !produto.getLotes().isEmpty()) {
            produto.updateQuantidadeTotal();
        }
        
        return repository.save(produto);
    }

    public Produto update(Integer id, Produto produto) {
        Produto existing = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Produto not found with id: " + id));
        
        existing.setNome(produto.getNome());
        existing.setValidadeEmMeses(produto.getValidadeEmMeses());
        
        // Update categoria if provided
        if (produto.getCategoria() != null && produto.getCategoria().getIdCategoria() != null) {
            Categoria categoria = categoriaRepository.findById(produto.getCategoria().getIdCategoria())
                .orElseThrow(() -> new IllegalArgumentException("Categoria not found with id: " + produto.getCategoria().getIdCategoria()));
            existing.setCategoria(categoria);
        }
        
        // Update quantidade total
        existing.updateQuantidadeTotal();
        
        return repository.save(existing);
    }

    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Produto not found with id: " + id);
        }
        repository.deleteById(id);
    }
}

