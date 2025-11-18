package com.QuickStacker.back.repository;

import com.QuickStacker.back.entity.FormulaHasMateriaPrima;
import com.QuickStacker.back.entity.FormulaHasMateriaPrimaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormulaHasMateriaPrimaRepository extends JpaRepository<FormulaHasMateriaPrima, FormulaHasMateriaPrimaId> {
    @Query("SELECT f FROM FormulaHasMateriaPrima f WHERE f.id.formulaIdFormula = :formulaId")
    List<FormulaHasMateriaPrima> findByFormulaIdFormula(@Param("formulaId") Integer formulaId);
    
    @Query("SELECT f FROM FormulaHasMateriaPrima f WHERE f.id.materiaPrimaIdMateriaPrima = :materiaPrimaId")
    List<FormulaHasMateriaPrima> findByMateriaPrimaIdMateriaPrima(@Param("materiaPrimaId") Integer materiaPrimaId);
    
    @Query("SELECT f FROM FormulaHasMateriaPrima f WHERE f.id.formulaIdFormula = :formulaId AND f.id.materiaPrimaIdMateriaPrima = :materiaPrimaId")
    Optional<FormulaHasMateriaPrima> findByFormulaIdFormulaAndMateriaPrimaIdMateriaPrima(
        @Param("formulaId") Integer formulaId, 
        @Param("materiaPrimaId") Integer materiaPrimaId
    );
    
    @Modifying
    @Transactional
    @Query("DELETE FROM FormulaHasMateriaPrima f WHERE f.id.formulaIdFormula = :formulaId")
    void deleteByFormulaIdFormula(@Param("formulaId") Integer formulaId);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM FormulaHasMateriaPrima f WHERE f.id.materiaPrimaIdMateriaPrima = :materiaPrimaId")
    void deleteByMateriaPrimaIdMateriaPrima(@Param("materiaPrimaId") Integer materiaPrimaId);
}

