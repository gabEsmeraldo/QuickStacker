package com.QuickStacker.back.repository;

import com.QuickStacker.back.entity.Formula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FormulaRepository extends JpaRepository<Formula, Integer> {
    Optional<Formula> findByProdutoIdProduto(Integer produtoId);
}

