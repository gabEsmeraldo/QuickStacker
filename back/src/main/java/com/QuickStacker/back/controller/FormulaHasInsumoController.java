package com.QuickStacker.back.controller;

import com.QuickStacker.back.entity.FormulaHasInsumo;
import com.QuickStacker.back.service.FormulaHasInsumoService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/formulas-insumos")
@CrossOrigin(origins = "*")
public class FormulaHasInsumoController {

  @Autowired
  private FormulaHasInsumoService formulaHasInsumoService;

  @GetMapping
  public ResponseEntity<List<FormulaHasInsumo>> getAllFormulaHasInsumos() {
    List<FormulaHasInsumo> formulas = formulaHasInsumoService.findAll();
    return ResponseEntity.ok(formulas);
  }

  @GetMapping("/formula/{formulaId}/insumo/{insumoId}")
  public ResponseEntity<FormulaHasInsumo> getFormulaHasInsumoById(
    @PathVariable Integer formulaId,
    @PathVariable Integer insumoId
  ) {
    Optional<FormulaHasInsumo> formula = formulaHasInsumoService.findById(formulaId, insumoId);
    return formula
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/formula/{formulaId}")
  public ResponseEntity<List<FormulaHasInsumo>> getFormulaHasInsumosByFormulaId(
    @PathVariable Integer formulaId
  ) {
    List<FormulaHasInsumo> formulas = formulaHasInsumoService.findByFormulaId(formulaId);
    return ResponseEntity.ok(formulas);
  }

  @GetMapping("/insumo/{insumoId}")
  public ResponseEntity<List<FormulaHasInsumo>> getFormulaHasInsumosByInsumoId(
    @PathVariable Integer insumoId
  ) {
    List<FormulaHasInsumo> formulas = formulaHasInsumoService.findByInsumoId(insumoId);
    return ResponseEntity.ok(formulas);
  }

  @PostMapping
  public ResponseEntity<FormulaHasInsumo> createFormulaHasInsumo(
    @RequestParam Integer formulaId,
    @RequestParam Integer insumoId,
    @RequestParam Integer quantidadeUtilizada
  ) {
    FormulaHasInsumo created = formulaHasInsumoService.create(formulaId, insumoId, quantidadeUtilizada);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PutMapping("/formula/{formulaId}/insumo/{insumoId}")
  public ResponseEntity<FormulaHasInsumo> updateFormulaHasInsumo(
    @PathVariable Integer formulaId,
    @PathVariable Integer insumoId,
    @RequestParam Integer quantidadeUtilizada
  ) {
    try {
      FormulaHasInsumo updated = formulaHasInsumoService.update(formulaId, insumoId, quantidadeUtilizada);
      return ResponseEntity.ok(updated);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/formula/{formulaId}/insumo/{insumoId}")
  public ResponseEntity<Void> deleteFormulaHasInsumo(
    @PathVariable Integer formulaId,
    @PathVariable Integer insumoId
  ) {
    try {
      formulaHasInsumoService.delete(formulaId, insumoId);
      return ResponseEntity.noContent().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/formula/{formulaId}")
  public ResponseEntity<Void> deleteAllByFormulaId(@PathVariable Integer formulaId) {
    try {
      formulaHasInsumoService.deleteByFormulaId(formulaId);
      return ResponseEntity.noContent().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/insumo/{insumoId}")
  public ResponseEntity<Void> deleteAllByInsumoId(@PathVariable Integer insumoId) {
    try {
      formulaHasInsumoService.deleteByInsumoId(insumoId);
      return ResponseEntity.noContent().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }
}

