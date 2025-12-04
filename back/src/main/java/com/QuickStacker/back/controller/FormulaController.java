package com.QuickStacker.back.controller;

import com.QuickStacker.back.dto.FormulaDTO;
import com.QuickStacker.back.entity.Formula;
import com.QuickStacker.back.service.FormulaService;
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
@RequestMapping("/api/formulas")
@CrossOrigin(origins = "*")
public class FormulaController {

  @Autowired
  private FormulaService formulaService;

  @GetMapping
  public ResponseEntity<List<Formula>> getAllFormulas() {
    List<Formula> formulas = formulaService.findAll();
    return ResponseEntity.ok(formulas);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Formula> getFormulaById(@PathVariable Integer id) {
    Optional<Formula> formula = formulaService.findById(id);
    return formula
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/produto/{produtoId}")
  public ResponseEntity<Formula> getFormulaByProdutoId(
    @PathVariable Integer produtoId
  ) {
    Optional<Formula> formula = formulaService.findByProdutoId(produtoId);
    return formula
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<?> createFormula(@Valid @RequestBody FormulaDTO dto) {
    try {
      System.out.println("[FormulaController] POST /api/formulas - Received DTO: produtoId=" + (dto != null ? dto.getProdutoId() : "null") + ", descricaoModoPreparo=" + (dto != null ? dto.getDescricaoModoPreparo() : "null"));
      if (dto == null) {
        System.out.println("[FormulaController] ERROR: DTO is null!");
        Map<String, String> error = new HashMap<>();
        error.put("error", "Request body is required");
        error.put("message", "Request body is required");
        return ResponseEntity.badRequest().body(error);
      }
      if (dto.getProdutoId() == null) {
        System.out.println("[FormulaController] ERROR: produtoId is null in DTO!");
        Map<String, String> error = new HashMap<>();
        error.put("error", "Produto ID is required");
        error.put("message", "Produto ID is required");
        return ResponseEntity.badRequest().body(error);
      }
      System.out.println("[FormulaController] Calling createFromDTO with produtoId=" + dto.getProdutoId());
      Formula created = formulaService.createFromDTO(dto);
      System.out.println("[FormulaController] Successfully created formula with id=" + created.getIdFormula());
      return ResponseEntity.status(HttpStatus.CREATED).body(created);
    } catch (IllegalArgumentException e) {
      System.out.println("[FormulaController] IllegalArgumentException: " + e.getMessage());
      e.printStackTrace();
      Map<String, String> error = new HashMap<>();
      error.put("error", e.getMessage());
      error.put("message", e.getMessage());
      return ResponseEntity.badRequest().body(error);
    } catch (Exception e) {
      System.out.println("[FormulaController] Unexpected error: " + e.getClass().getName() + " - " + e.getMessage());
      e.printStackTrace();
      Map<String, String> error = new HashMap<>();
      error.put("error", "Internal server error: " + e.getMessage());
      error.put("message", "Internal server error: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateFormula(
    @PathVariable Integer id,
    @Valid @RequestBody FormulaDTO dto
  ) {
    try {
      Formula updated = formulaService.updateFromDTO(id, dto);
      return ResponseEntity.ok(updated);
    } catch (IllegalArgumentException e) {
      Map<String, String> error = new HashMap<>();
      error.put("error", e.getMessage());
      error.put("message", e.getMessage());
      return ResponseEntity.badRequest().body(error);
    } catch (Exception e) {
      Map<String, String> error = new HashMap<>();
      error.put("error", "Internal server error: " + e.getMessage());
      error.put("message", "Internal server error: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteFormula(@PathVariable Integer id) {
    try {
      formulaService.delete(id);
      return ResponseEntity.noContent().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }
}
