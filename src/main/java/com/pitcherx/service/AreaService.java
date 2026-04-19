package com.pitcherx.service;

import com.pitcherx.dto.area.AreaRequestDTO;
import com.pitcherx.dto.area.AreaResponseDTO;
import com.pitcherx.model.Area;
import com.pitcherx.repository.AreaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AreaService {

    private final AreaRepository areaRepository;

    public AreaService(AreaRepository areaRepository){
        this.areaRepository = areaRepository;
    }

    @Transactional(readOnly = true)
    public List<AreaResponseDTO> listarAreas(){
        return areaRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AreaResponseDTO buscarAreaPorId(Long idArea){
        Area area = areaRepository.findById(idArea).orElseThrow(() -> new EntityNotFoundException("Sem area com o ID informado!"));
        return toResponse(area);
    }

    @Transactional
    public AreaResponseDTO criarArea(AreaRequestDTO areaRequestDTO){

        if (areaRepository.existsByNomeArea(areaRequestDTO.nomeArea())){
            throw new IllegalArgumentException("Já existe uma área com esse nome!");
        }

        Area area = new Area();

        area.setNomeArea(areaRequestDTO.nomeArea());
        area.setDescricaoArea(areaRequestDTO.descricaoArea());

        Area salvo = areaRepository.save(area);
        return toResponse(salvo);
    }

    @Transactional
    public AreaResponseDTO atualizarArea(Long idArea, AreaRequestDTO areaRequestDTO){
        Area area = areaRepository.findById(idArea).orElseThrow(() -> new EntityNotFoundException("Sem area com o ID informado!"));

        if (areaRepository.existsByNomeAreaAndIdAreaNot(areaRequestDTO.nomeArea(), idArea)){
            throw new IllegalArgumentException("Já existe uma área com esse nome!");
        }

        area.setNomeArea(areaRequestDTO.nomeArea());
        area.setDescricaoArea(areaRequestDTO.descricaoArea());

        Area salvo = areaRepository.save(area);
        return toResponse(salvo);
    }

    @Transactional
    public void deletarArea(Long idArea){
        if (!areaRepository.existsById(idArea)){
            throw new EntityNotFoundException("Sem area com o ID informado!");
        }

        try {
            areaRepository.deleteById(idArea);
        }catch (DataIntegrityViolationException e) {
            // Caso a área estiver ligada a outras tabelas
            throw new IllegalArgumentException("Não é possível deletar esta área, pois existem subAreas e projetos vinculados a ela.");
        }

    }

    public AreaResponseDTO toResponse(Area area){
        return new AreaResponseDTO(
                area.getIdArea(),
                area.getNomeArea(),
                area.getDescricaoArea()
        );
    }

}
