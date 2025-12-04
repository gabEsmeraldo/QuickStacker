package com.QuickStacker.back.service;

import com.QuickStacker.back.dto.ProdutoDTO;
import com.QuickStacker.back.entity.Categoria;
import com.QuickStacker.back.entity.Produto;
import com.QuickStacker.back.repository.CategoriaRepository;
import com.QuickStacker.back.repository.ProdutoRepository;
import jakarta.persistence.EntityManager;
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

    @Autowired
    private EntityManager entityManager;

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
        if (produto.getCategoria() == null || produto.getCategoria().getIdCategoria() == null) {
            throw new IllegalArgumentException("A Categoria é obrigatória para cadastrar um produto.");
        }

        Categoria categoria = categoriaRepository.findById(produto.getCategoria().getIdCategoria())
            .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada com o ID: " + produto.getCategoria().getIdCategoria()));
        
        produto.setCategoria(categoria);

        if (produto.getLotes() != null && !produto.getLotes().isEmpty()) {
            produto.updateQuantidadeTotal();
        }
        
        return repository.save(produto);
    }

    public Produto createFromDTO(ProdutoDTO dto) {
        System.out.println("[ProdutoService] createFromDTO called with categoriaId=" + (dto != null ? dto.getCategoriaId() : "null"));
        // Validate categoriaId is required
        if (dto == null || dto.getCategoriaId() == null) {
            System.out.println("[ProdutoService] ERROR: categoriaId is null!");
            throw new IllegalArgumentException("Categoria ID is required");
        }
        
        System.out.println("[ProdutoService] Looking up categoria with id=" + dto.getCategoriaId());
        // Validate categoria exists
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
            .orElseThrow(() -> {
                System.out.println("[ProdutoService] ERROR: Categoria not found with id=" + dto.getCategoriaId());
                return new IllegalArgumentException("Categoria not found with id: " + dto.getCategoriaId());
            });
        
        System.out.println("[ProdutoService] Categoria found: " + categoria.getDescricao() + " (ID: " + categoria.getIdCategoria() + ")");
        
        // Create produto and set categoria - use the categoria from findById (should be managed)
        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setValidadeEmMeses(dto.getValidadeEmMeses());
        
        // Explicitly set the categoria - ensure it's the one from the repository (managed entity)
        produto.setCategoria(categoria);
        
        // Verify categoria is set before save
        if (produto.getCategoria() == null || produto.getCategoria().getIdCategoria() == null) {
            throw new IllegalStateException("Categoria was not set on produto before save!");
        }
        
        System.out.println("[ProdutoService] Before save - Produto categoria ID: " + produto.getCategoria().getIdCategoria());
        
        Produto saved = repository.save(produto);
        System.out.println("[ProdutoService] After save - Produto saved with id=" + saved.getIdProduto());
        return saved;
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

