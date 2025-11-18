package com.QuickStacker.back.controller;

import com.QuickStacker.back.entity.Produto;
import com.QuickStacker.back.service.ProdutoService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {

  @Autowired
  private ProdutoService produtoService;

  @GetMapping
  public ResponseEntity<List<Produto>> getAllProdutos() {
    List<Produto> produtos = produtoService.findAll();
    return ResponseEntity.ok(produtos);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Produto> getProdutoById(@PathVariable Integer id) {
    Optional<Produto> produto = produtoService.findById(id);
    return produto
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/nome/{nome}")
  public ResponseEntity<Produto> getProdutoByNome(@PathVariable String nome) {
    Optional<Produto> produto = produtoService.findByNome(nome);
    return produto
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/categoria/{categoriaId}")
  public ResponseEntity<List<Produto>> getProdutosByCategoriaId(
    @PathVariable Integer categoriaId
  ) {
    List<Produto> produtos = produtoService.findByCategoriaId(categoriaId);
    return ResponseEntity.ok(produtos);
  }

  @PostMapping
  public ResponseEntity<Produto> createProduto(@RequestBody Produto produto) {
    Produto created = produtoService.create(produto);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Produto> updateProduto(
    @PathVariable Integer id,
    @RequestBody Produto produto
  ) {
    try {
      Produto updated = produtoService.update(id, produto);
      return ResponseEntity.ok(updated);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProduto(@PathVariable Integer id) {
    try {
      produtoService.delete(id);
      return ResponseEntity.noContent().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }
}

