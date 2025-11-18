package com.QuickStacker.back.repository;

import com.QuickStacker.back.entity.LoteInsumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoteInsumoRepository extends JpaRepository<LoteInsumo, String> {
    List<LoteInsumo> findByInsumoIdInsumo(Integer insumoId);
    List<LoteInsumo> findByDataChegadaBetween(LocalDate start, LocalDate end);
    Optional<LoteInsumo> findByIdLoteInsumo(String idLoteInsumo);
}

