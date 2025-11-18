package com.QuickStacker.back.repository;

import com.QuickStacker.back.entity.Lote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoteRepository extends JpaRepository<Lote, Integer> {
    List<Lote> findByProdutoIdProduto(Integer produtoId);
    List<Lote> findByDataValidadeBefore(LocalDate date);
    List<Lote> findByDataValidadeBetween(LocalDate start, LocalDate end);
}

