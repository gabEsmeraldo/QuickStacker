package com.QuickStacker.back.repository;

import com.QuickStacker.back.entity.MateriaPrima;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MateriaPrimaRepository extends JpaRepository<MateriaPrima, Integer> {
    Optional<MateriaPrima> findByNome(String nome);
}

