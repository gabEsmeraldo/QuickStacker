package com.QuickStacker.back.controller;

import com.QuickStacker.back.entity.MateriaPrima;
import com.QuickStacker.back.service.MateriaPrimaService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/materias-primas")
@CrossOrigin(origins = "*")
public class MateriaPrimaController {

  @Autowired
  private MateriaPrimaService materiaPrimaService;

  @GetMapping
  public ResponseEntity<List<MateriaPrima>> getAllMateriasPrimas() {
    List<MateriaPrima> materiasPrimas = materiaPrimaService.findAll();
    return ResponseEntity.ok(materiasPrimas);
  }

  @GetMapping("/{id}")
  public ResponseEntity<MateriaPrima> getMateriaPrimaById(@PathVariable Integer id) {
    Optional<MateriaPrima> materiaPrima = materiaPrimaService.findById(id);
    return materiaPrima
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/nome/{nome}")
  public ResponseEntity<MateriaPrima> getMateriaPrimaByNome(@PathVariable String nome) {
    Optional<MateriaPrima> materiaPrima = materiaPrimaService.findByNome(nome);
    return materiaPrima
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<MateriaPrima> createMateriaPrima(@RequestBody MateriaPrima materiaPrima) {
    MateriaPrima created = materiaPrimaService.create(materiaPrima);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PutMapping("/{id}")
  public ResponseEntity<MateriaPrima> updateMateriaPrima(
    @PathVariable Integer id,
    @RequestBody MateriaPrima materiaPrima
  ) {
    try {
      MateriaPrima updated = materiaPrimaService.update(id, materiaPrima);
      return ResponseEntity.ok(updated);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteMateriaPrima(@PathVariable Integer id) {
    try {
      materiaPrimaService.delete(id);
      return ResponseEntity.noContent().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }
}

