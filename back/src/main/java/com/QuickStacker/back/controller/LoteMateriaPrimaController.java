package com.QuickStacker.back.controller;

import com.QuickStacker.back.entity.LoteMateriaPrima;
import com.QuickStacker.back.service.LoteMateriaPrimaService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lotes-materias-primas")
@CrossOrigin(origins = "*")
public class LoteMateriaPrimaController {

  @Autowired
  private LoteMateriaPrimaService loteMateriaPrimaService;

  @GetMapping
  public ResponseEntity<List<LoteMateriaPrima>> getAllLotesMateriasPrimas() {
    List<LoteMateriaPrima> lotes = loteMateriaPrimaService.findAll();
    return ResponseEntity.ok(lotes);
  }

  @GetMapping("/{id}")
  public ResponseEntity<LoteMateriaPrima> getLoteMateriaPrimaById(@PathVariable String id) {
    Optional<LoteMateriaPrima> lote = loteMateriaPrimaService.findById(id);
    return lote
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/materia-prima/{materiaPrimaId}")
  public ResponseEntity<List<LoteMateriaPrima>> getLotesByMateriaPrimaId(
    @PathVariable Integer materiaPrimaId
  ) {
    List<LoteMateriaPrima> lotes = loteMateriaPrimaService.findByMateriaPrimaId(materiaPrimaId);
    return ResponseEntity.ok(lotes);
  }

  @GetMapping("/expired")
  public ResponseEntity<List<LoteMateriaPrima>> getExpiredLotes() {
    List<LoteMateriaPrima> lotes = loteMateriaPrimaService.findExpired();
    return ResponseEntity.ok(lotes);
  }

  @GetMapping("/expiring")
  public ResponseEntity<List<LoteMateriaPrima>> getExpiringLotes(
    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
  ) {
    List<LoteMateriaPrima> lotes = loteMateriaPrimaService.findExpiringBetween(start, end);
    return ResponseEntity.ok(lotes);
  }

  @GetMapping("/fornecedor/{fornecedor}")
  public ResponseEntity<List<LoteMateriaPrima>> getLotesByFornecedor(
    @PathVariable String fornecedor
  ) {
    List<LoteMateriaPrima> lotes = loteMateriaPrimaService.findByFornecedor(fornecedor);
    return ResponseEntity.ok(lotes);
  }

  @PostMapping
  public ResponseEntity<LoteMateriaPrima> createLoteMateriaPrima(@RequestBody LoteMateriaPrima lote) {
    LoteMateriaPrima created = loteMateriaPrimaService.create(lote);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PutMapping("/{id}")
  public ResponseEntity<LoteMateriaPrima> updateLoteMateriaPrima(
    @PathVariable String id,
    @RequestBody LoteMateriaPrima lote
  ) {
    try {
      LoteMateriaPrima updated = loteMateriaPrimaService.update(id, lote);
      return ResponseEntity.ok(updated);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteLoteMateriaPrima(@PathVariable String id) {
    try {
      loteMateriaPrimaService.delete(id);
      return ResponseEntity.noContent().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }
}

