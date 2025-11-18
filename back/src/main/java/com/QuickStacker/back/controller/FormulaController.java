package com.QuickStacker.back.controller;

import com.QuickStacker.back.entity.Formula;
import com.QuickStacker.back.service.FormulaService;
import java.util.List;
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
  public ResponseEntity<Formula> createFormula(@RequestBody Formula formula) {
    Formula created = formulaService.create(formula);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Formula> updateFormula(
    @PathVariable Integer id,
    @RequestBody Formula formula
  ) {
    try {
      Formula updated = formulaService.update(id, formula);
      return ResponseEntity.ok(updated);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
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
