package com.QuickStacker.back.repository;

import com.QuickStacker.back.entity.LoteMateriaPrima;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoteMateriaPrimaRepository extends JpaRepository<LoteMateriaPrima, String> {
    List<LoteMateriaPrima> findByMateriaPrimaIdMateriaPrima(Integer materiaPrimaId);
    List<LoteMateriaPrima> findByDataValidadeBefore(LocalDate date);
    List<LoteMateriaPrima> findByDataValidadeBetween(LocalDate start, LocalDate end);
    List<LoteMateriaPrima> findByFornecedor(String fornecedor);
    Optional<LoteMateriaPrima> findByIdLoteMP(String idLoteMP);
}

