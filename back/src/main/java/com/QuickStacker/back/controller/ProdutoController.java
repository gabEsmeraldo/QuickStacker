package com.QuickStacker.back.controller;

import com.QuickStacker.back.dto.ProdutoDTO;
import com.QuickStacker.back.entity.Produto;
import com.QuickStacker.back.service.ProdutoService;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
  public ResponseEntity<?> createProduto(@Valid @RequestBody ProdutoDTO dto) {
    try {
      System.out.println("[ProdutoController] POST /api/produtos - Received DTO: categoriaId=" + (dto != null ? dto.getCategoriaId() : "null") + ", nome=" + (dto != null ? dto.getNome() : "null"));
      if (dto == null) {
        System.out.println("[ProdutoController] ERROR: DTO is null!");
        Map<String, String> error = new HashMap<>();
        error.put("error", "Request body is required");
        error.put("message", "Request body is required");
        return ResponseEntity.badRequest().body(error);
      }
      if (dto.getCategoriaId() == null) {
        System.out.println("[ProdutoController] ERROR: categoriaId is null in DTO!");
        Map<String, String> error = new HashMap<>();
        error.put("error", "Categoria ID is required");
        error.put("message", "Categoria ID is required");
        return ResponseEntity.badRequest().body(error);
      }
      Produto created = produtoService.createFromDTO(dto);
      System.out.println("[ProdutoController] Successfully created produto with id=" + created.getIdProduto());
      return ResponseEntity.status(HttpStatus.CREATED).body(created);
    } catch (IllegalArgumentException e) {
      System.out.println("[ProdutoController] IllegalArgumentException: " + e.getMessage());
      Map<String, String> error = new HashMap<>();
      error.put("error", e.getMessage());
      error.put("message", e.getMessage());
      return ResponseEntity.badRequest().body(error);
    } catch (Exception e) {
      System.out.println("[ProdutoController] Unexpected error: " + e.getClass().getName() + " - " + e.getMessage());
      e.printStackTrace();
      Map<String, String> error = new HashMap<>();
      error.put("error", "Internal server error: " + e.getMessage());
      error.put("message", "Internal server error: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
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

