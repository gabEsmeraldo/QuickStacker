package com.QuickStacker.back.repository;

import com.QuickStacker.back.entity.FormulaHasInsumo;
import com.QuickStacker.back.entity.FormulaHasInsumoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormulaHasInsumoRepository extends JpaRepository<FormulaHasInsumo, FormulaHasInsumoId> {
    @Query("SELECT f FROM FormulaHasInsumo f WHERE f.formula.idFormula = :formulaId")
    List<FormulaHasInsumo> findByFormulaIdFormula(@Param("formulaId") Integer formulaId);
    
    @Query("SELECT f FROM FormulaHasInsumo f WHERE f.insumo.idInsumo = :insumoId")
    List<FormulaHasInsumo> findByInsumoIdInsumo(@Param("insumoId") Integer insumoId);
    
    @Query("SELECT f FROM FormulaHasInsumo f WHERE f.formula.idFormula = :formulaId AND f.insumo.idInsumo = :insumoId")
    Optional<FormulaHasInsumo> findByFormulaIdFormulaAndInsumoIdInsumo(
        @Param("formulaId") Integer formulaId, 
        @Param("insumoId") Integer insumoId
    );
    
    @Modifying
    @Transactional
    @Query("DELETE FROM FormulaHasInsumo f WHERE f.formula.idFormula = :formulaId")
    void deleteByFormulaIdFormula(@Param("formulaId") Integer formulaId);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM FormulaHasInsumo f WHERE f.insumo.idInsumo = :insumoId")
    void deleteByInsumoIdInsumo(@Param("insumoId") Integer insumoId);
}

