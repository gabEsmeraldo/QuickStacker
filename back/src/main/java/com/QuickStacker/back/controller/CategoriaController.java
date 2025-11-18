package com.QuickStacker.back.controller;

import com.QuickStacker.back.entity.Categoria;
import com.QuickStacker.back.service.CategoriaService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
public class CategoriaController {

  @Autowired
  private CategoriaService categoriaService;

  @GetMapping
  public ResponseEntity<List<Categoria>> getAllCategorias() {
    List<Categoria> categorias = categoriaService.findAll();
    return ResponseEntity.ok(categorias);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Categoria> getCategoriaById(@PathVariable Integer id) {
    Optional<Categoria> categoria = categoriaService.findById(id);
    return categoria
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Categoria> createCategoria(@RequestBody Categoria categoria) {
    Categoria created = categoriaService.create(categoria);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Categoria> updateCategoria(
    @PathVariable Integer id,
    @RequestBody Categoria categoria
  ) {
    try {
      Categoria updated = categoriaService.update(id, categoria);
      return ResponseEntity.ok(updated);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCategoria(@PathVariable Integer id) {
    try {
      categoriaService.delete(id);
      return ResponseEntity.noContent().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }
}

