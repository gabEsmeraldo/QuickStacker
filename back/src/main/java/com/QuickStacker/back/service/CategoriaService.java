package com.QuickStacker.back.service;

import com.QuickStacker.back.entity.Categoria;
import com.QuickStacker.back.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoriaService {

    @Autowired
    private CategoriaRepository repository;

    public List<Categoria> findAll() {
        return repository.findAll();
    }

    public Optional<Categoria> findById(Integer id) {
        return repository.findById(id);
    }

    public Categoria create(Categoria categoria) {
        return repository.save(categoria);
    }

    public Categoria update(Integer id, Categoria categoria) {
        Categoria existing = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Categoria not found with id: " + id));
        
        existing.setDescricao(categoria.getDescricao());
        
        return repository.save(existing);
    }

    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Categoria not found with id: " + id);
        }
        repository.deleteById(id);
    }
}

