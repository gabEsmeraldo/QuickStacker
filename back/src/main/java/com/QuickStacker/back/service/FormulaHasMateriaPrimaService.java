package com.QuickStacker.back.service;

import com.QuickStacker.back.dto.FormulaHasMateriaPrimaDTO;
import com.QuickStacker.back.entity.Formula;
import com.QuickStacker.back.entity.FormulaHasMateriaPrima;
import com.QuickStacker.back.entity.FormulaHasMateriaPrimaId;
import com.QuickStacker.back.entity.MateriaPrima;
import com.QuickStacker.back.repository.FormulaHasMateriaPrimaRepository;
import com.QuickStacker.back.repository.FormulaRepository;
import com.QuickStacker.back.repository.MateriaPrimaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class FormulaHasMateriaPrimaService {

    @Autowired
    private FormulaHasMateriaPrimaRepository repository;

    @Autowired
    private FormulaRepository formulaRepository;

    @Autowired
    private MateriaPrimaRepository materiaPrimaRepository;

    // Mapper methods
    /**
     * Converts FormulaHasMateriaPrima entity to DTO
     */
    public FormulaHasMateriaPrimaDTO toDTO(FormulaHasMateriaPrima entity) {
        if (entity == null) {
            return null;
        }

        FormulaHasMateriaPrimaDTO dto = new FormulaHasMateriaPrimaDTO();
        
        if (entity.getFormula() != null) {
            dto.setFormulaId(entity.getFormula().getIdFormula());
        }
        
        if (entity.getMateriaPrima() != null) {
            dto.setMateriaPrimaId(entity.getMateriaPrima().getIdMateriaPrima());
        }
        
        dto.setQuantidadeUtilizada(entity.getQuantidadeUtilizada());
        
        return dto;
    }

    /**
     * Converts DTO to FormulaHasMateriaPrima entity
     */
    private FormulaHasMateriaPrima toEntity(FormulaHasMateriaPrimaDTO dto) {
        if (dto == null) {
            return null;
        }

        Formula formula = formulaRepository.findById(dto.getFormulaId())
            .orElseThrow(() -> new IllegalArgumentException("Formula not found with id: " + dto.getFormulaId()));
        
        MateriaPrima materiaPrima = materiaPrimaRepository.findById(dto.getMateriaPrimaId())
            .orElseThrow(() -> new IllegalArgumentException("MateriaPrima not found with id: " + dto.getMateriaPrimaId()));

        return new FormulaHasMateriaPrima(
            formula,
            materiaPrima,
            dto.getQuantidadeUtilizada()
        );
    }

    // CRUD operations
    public List<FormulaHasMateriaPrimaDTO> findAll() {
        return repository.findAll().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public Optional<FormulaHasMateriaPrimaDTO> findById(Integer formulaId, Integer materiaPrimaId) {
        return repository.findByFormulaIdFormulaAndMateriaPrimaIdMateriaPrima(formulaId, materiaPrimaId)
            .map(this::toDTO);
    }

    public List<FormulaHasMateriaPrimaDTO> findByFormulaId(Integer formulaId) {
        return repository.findByFormulaIdFormula(formulaId).stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public List<FormulaHasMateriaPrimaDTO> findByMateriaPrimaId(Integer materiaPrimaId) {
        return repository.findByMateriaPrimaIdMateriaPrima(materiaPrimaId).stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public FormulaHasMateriaPrimaDTO create(FormulaHasMateriaPrimaDTO dto) {
        FormulaHasMateriaPrima entity = toEntity(dto);
        FormulaHasMateriaPrima saved = repository.save(entity);
        return toDTO(saved);
    }

    public FormulaHasMateriaPrimaDTO update(Integer formulaId, Integer materiaPrimaId, FormulaHasMateriaPrimaDTO dto) {
        FormulaHasMateriaPrima entity = repository.findByFormulaIdFormulaAndMateriaPrimaIdMateriaPrima(formulaId, materiaPrimaId)
            .orElseThrow(() -> new IllegalArgumentException(
                "FormulaHasMateriaPrima not found with formulaId: " + formulaId + " and materiaPrimaId: " + materiaPrimaId));

        if (dto.getQuantidadeUtilizada() != null) {
            entity.setQuantidadeUtilizada(dto.getQuantidadeUtilizada());
        }

        FormulaHasMateriaPrima updated = repository.save(entity);
        return toDTO(updated);
    }

    public void delete(Integer formulaId, Integer materiaPrimaId) {
        FormulaHasMateriaPrimaId id = new FormulaHasMateriaPrimaId(formulaId, materiaPrimaId);
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException(
                "FormulaHasMateriaPrima not found with formulaId: " + formulaId + " and materiaPrimaId: " + materiaPrimaId);
        }
        repository.deleteById(id);
    }

    public void deleteByFormulaId(Integer formulaId) {
        repository.deleteByFormulaIdFormula(formulaId);
    }

    public void deleteByMateriaPrimaId(Integer materiaPrimaId) {
        repository.deleteByMateriaPrimaIdMateriaPrima(materiaPrimaId);
    }
}

