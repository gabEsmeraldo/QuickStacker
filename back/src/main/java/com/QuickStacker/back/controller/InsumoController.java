package com.QuickStacker.back.controller;

import com.QuickStacker.back.entity.Insumo;
import com.QuickStacker.back.service.InsumoService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/insumos")
@CrossOrigin(origins = "*")
public class InsumoController {

  @Autowired
  private InsumoService insumoService;

  @GetMapping
  public ResponseEntity<List<Insumo>> getAllInsumos() {
    List<Insumo> insumos = insumoService.findAll();
    return ResponseEntity.ok(insumos);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Insumo> getInsumoById(@PathVariable Integer id) {
    Optional<Insumo> insumo = insumoService.findById(id);
    return insumo
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/nome/{nome}")
  public ResponseEntity<Insumo> getInsumoByNome(@PathVariable String nome) {
    Optional<Insumo> insumo = insumoService.findByNome(nome);
    return insumo
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Insumo> createInsumo(@RequestBody Insumo insumo) {
    Insumo created = insumoService.create(insumo);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Insumo> updateInsumo(
    @PathVariable Integer id,
    @RequestBody Insumo insumo
  ) {
    try {
      Insumo updated = insumoService.update(id, insumo);
      return ResponseEntity.ok(updated);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteInsumo(@PathVariable Integer id) {
    try {
      insumoService.delete(id);
      return ResponseEntity.noContent().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }
}

