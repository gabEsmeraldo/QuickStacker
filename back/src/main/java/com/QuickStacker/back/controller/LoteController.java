package com.QuickStacker.back.controller;

import com.QuickStacker.back.entity.Lote;
import com.QuickStacker.back.service.LoteService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lotes")
@CrossOrigin(origins = "*")
public class LoteController {

  @Autowired
  private LoteService loteService;

  @GetMapping
  public ResponseEntity<List<Lote>> getAllLotes() {
    List<Lote> lotes = loteService.findAll();
    return ResponseEntity.ok(lotes);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Lote> getLoteById(@PathVariable Integer id) {
    Optional<Lote> lote = loteService.findById(id);
    return lote
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/produto/{produtoId}")
  public ResponseEntity<List<Lote>> getLotesByProdutoId(
    @PathVariable Integer produtoId
  ) {
    List<Lote> lotes = loteService.findByProdutoId(produtoId);
    return ResponseEntity.ok(lotes);
  }

  @GetMapping("/expired")
  public ResponseEntity<List<Lote>> getExpiredLotes() {
    List<Lote> lotes = loteService.findExpired();
    return ResponseEntity.ok(lotes);
  }

  @GetMapping("/expiring")
  public ResponseEntity<List<Lote>> getExpiringLotes(
    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
  ) {
    List<Lote> lotes = loteService.findExpiringBetween(start, end);
    return ResponseEntity.ok(lotes);
  }

  @PostMapping
  public ResponseEntity<Lote> createLote(@RequestBody Lote lote) {
    Lote created = loteService.create(lote);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Lote> updateLote(
    @PathVariable Integer id,
    @RequestBody Lote lote
  ) {
    try {
      Lote updated = loteService.update(id, lote);
      return ResponseEntity.ok(updated);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteLote(@PathVariable Integer id) {
    try {
      loteService.delete(id);
      return ResponseEntity.noContent().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }
}

