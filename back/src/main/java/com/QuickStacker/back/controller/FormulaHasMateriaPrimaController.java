package com.QuickStacker.back.controller;

import com.QuickStacker.back.dto.FormulaHasMateriaPrimaDTO;
import com.QuickStacker.back.service.FormulaHasMateriaPrimaService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/formulas-materias-primas")
@CrossOrigin(origins = "*")
public class FormulaHasMateriaPrimaController {

  @Autowired
  private FormulaHasMateriaPrimaService formulaHasMateriaPrimaService;

  @GetMapping
  public ResponseEntity<List<FormulaHasMateriaPrimaDTO>> getAllFormulaHasMateriaPrimas() {
    List<FormulaHasMateriaPrimaDTO> formulas = formulaHasMateriaPrimaService.findAll();
    return ResponseEntity.ok(formulas);
  }

  @GetMapping("/formula/{formulaId}/materia-prima/{materiaPrimaId}")
  public ResponseEntity<FormulaHasMateriaPrimaDTO> getFormulaHasMateriaPrimaById(
    @PathVariable Integer formulaId,
    @PathVariable Integer materiaPrimaId
  ) {
    Optional<FormulaHasMateriaPrimaDTO> formula = formulaHasMateriaPrimaService.findById(formulaId, materiaPrimaId);
    return formula
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/formula/{formulaId}")
  public ResponseEntity<List<FormulaHasMateriaPrimaDTO>> getFormulaHasMateriaPrimasByFormulaId(
    @PathVariable Integer formulaId
  ) {
    List<FormulaHasMateriaPrimaDTO> formulas = formulaHasMateriaPrimaService.findByFormulaId(formulaId);
    return ResponseEntity.ok(formulas);
  }

  @GetMapping("/materia-prima/{materiaPrimaId}")
  public ResponseEntity<List<FormulaHasMateriaPrimaDTO>> getFormulaHasMateriaPrimasByMateriaPrimaId(
    @PathVariable Integer materiaPrimaId
  ) {
    List<FormulaHasMateriaPrimaDTO> formulas = formulaHasMateriaPrimaService.findByMateriaPrimaId(materiaPrimaId);
    return ResponseEntity.ok(formulas);
  }

  @PostMapping
  public ResponseEntity<FormulaHasMateriaPrimaDTO> createFormulaHasMateriaPrima(
    @RequestBody FormulaHasMateriaPrimaDTO dto
  ) {
    FormulaHasMateriaPrimaDTO created = formulaHasMateriaPrimaService.create(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PutMapping("/formula/{formulaId}/materia-prima/{materiaPrimaId}")
  public ResponseEntity<FormulaHasMateriaPrimaDTO> updateFormulaHasMateriaPrima(
    @PathVariable Integer formulaId,
    @PathVariable Integer materiaPrimaId,
    @RequestBody FormulaHasMateriaPrimaDTO dto
  ) {
    try {
      FormulaHasMateriaPrimaDTO updated = formulaHasMateriaPrimaService.update(formulaId, materiaPrimaId, dto);
      return ResponseEntity.ok(updated);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/formula/{formulaId}/materia-prima/{materiaPrimaId}")
  public ResponseEntity<Void> deleteFormulaHasMateriaPrima(
    @PathVariable Integer formulaId,
    @PathVariable Integer materiaPrimaId
  ) {
    try {
      formulaHasMateriaPrimaService.delete(formulaId, materiaPrimaId);
      return ResponseEntity.noContent().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/formula/{formulaId}")
  public ResponseEntity<Void> deleteAllByFormulaId(@PathVariable Integer formulaId) {
    try {
      formulaHasMateriaPrimaService.deleteByFormulaId(formulaId);
      return ResponseEntity.noContent().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/materia-prima/{materiaPrimaId}")
  public ResponseEntity<Void> deleteAllByMateriaPrimaId(@PathVariable Integer materiaPrimaId) {
    try {
      formulaHasMateriaPrimaService.deleteByMateriaPrimaId(materiaPrimaId);
      return ResponseEntity.noContent().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }
}

