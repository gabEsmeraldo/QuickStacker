package com.QuickStacker.back.controller;

import com.QuickStacker.back.entity.LoteInsumo;
import com.QuickStacker.back.service.LoteInsumoService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lotes-insumos")
@CrossOrigin(origins = "*")
public class LoteInsumoController {

  @Autowired
  private LoteInsumoService loteInsumoService;

  @GetMapping
  public ResponseEntity<List<LoteInsumo>> getAllLotesInsumos() {
    List<LoteInsumo> lotes = loteInsumoService.findAll();
    return ResponseEntity.ok(lotes);
  }

  @GetMapping("/{id}")
  public ResponseEntity<LoteInsumo> getLoteInsumoById(@PathVariable String id) {
    Optional<LoteInsumo> lote = loteInsumoService.findById(id);
    return lote
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/insumo/{insumoId}")
  public ResponseEntity<List<LoteInsumo>> getLotesByInsumoId(
    @PathVariable Integer insumoId
  ) {
    List<LoteInsumo> lotes = loteInsumoService.findByInsumoId(insumoId);
    return ResponseEntity.ok(lotes);
  }

  @GetMapping("/data-chegada")
  public ResponseEntity<List<LoteInsumo>> getLotesByDataChegada(
    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
  ) {
    List<LoteInsumo> lotes = loteInsumoService.findByDataChegadaBetween(start, end);
    return ResponseEntity.ok(lotes);
  }

  @PostMapping
  public ResponseEntity<LoteInsumo> createLoteInsumo(@RequestBody LoteInsumo lote) {
    LoteInsumo created = loteInsumoService.create(lote);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PutMapping("/{id}")
  public ResponseEntity<LoteInsumo> updateLoteInsumo(
    @PathVariable String id,
    @RequestBody LoteInsumo lote
  ) {
    try {
      LoteInsumo updated = loteInsumoService.update(id, lote);
      return ResponseEntity.ok(updated);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteLoteInsumo(@PathVariable String id) {
    try {
      loteInsumoService.delete(id);
      return ResponseEntity.noContent().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }
}

