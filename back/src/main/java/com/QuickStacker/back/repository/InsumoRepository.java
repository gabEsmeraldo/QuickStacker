package com.QuickStacker.back.repository;

import com.QuickStacker.back.entity.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InsumoRepository extends JpaRepository<Insumo, Integer> {
    Optional<Insumo> findByNome(String nome);
}

