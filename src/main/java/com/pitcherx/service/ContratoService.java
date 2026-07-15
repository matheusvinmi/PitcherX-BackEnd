package com.pitcherx.service;

import com.pitcherx.dto.contrato.ContratoRequestDTO;
import com.pitcherx.dto.contrato.ContratoResponseDTO;
import com.pitcherx.mapper.ContratoMapper;
import com.pitcherx.model.Contrato;
import com.pitcherx.model.Projeto;
import com.pitcherx.repository.ContratoRepository;
import com.pitcherx.repository.ProjetoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContratoService {

    private final ContratoRepository contratoRepository;
    private final ContratoMapper contratoMapper;
    private final ProjetoRepository projetoRepository;

    public ContratoService(ContratoRepository contratoRepository, ContratoMapper contratoMapper, ProjetoRepository projetoRepository) {
        this.contratoRepository = contratoRepository;
        this.contratoMapper = contratoMapper;
        this.projetoRepository = projetoRepository;
    }

    @Transactional(readOnly = true)
    public List<ContratoResponseDTO> listarContratos(){
        return contratoRepository.findAll().stream()
                .map(contratoMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ContratoResponseDTO buscarContratoPorId(Long idContrato) {
        Contrato contrato = contratoRepository.findById(idContrato)
                .orElseThrow(() -> new EntityNotFoundException("Sem contrato com o ID informado!"));
        return contratoMapper.toDTO(contrato);
    }

    @Transactional
    public ContratoResponseDTO criarContrato(ContratoRequestDTO contratoRequestDTO){
        Projeto projeto = projetoRepository.findById(contratoRequestDTO.idProjeto())
                .orElseThrow(() -> new EntityNotFoundException("Sem projeto com o ID informado!"));

        Contrato contrato = contratoMapper.toEntity(contratoRequestDTO);
        contrato.setProjeto(projeto);

        Contrato salvo = contratoRepository.save(contrato);
        return contratoMapper.toDTO(salvo);
    }

    @Transactional
    public ContratoResponseDTO atualizarContrato(Long idContrato, ContratoRequestDTO contratoRequestDTO) {
        Contrato contrato = contratoRepository.findById(idContrato)
                .orElseThrow(() -> new EntityNotFoundException("Sem contrato com o ID informado!"));

        Projeto projeto = projetoRepository.findById(contratoRequestDTO.idProjeto())
                .orElseThrow(() -> new EntityNotFoundException("Sem projeto com o ID informado!"));

        contrato.setProjeto(projeto);
        contratoMapper.toUpdate(contratoRequestDTO, contrato);

        Contrato salvo = contratoRepository.save(contrato);
        return contratoMapper.toDTO(salvo);
    }

    @Transactional
    public void deletarContrato(Long idContrato){
        if (!contratoRepository.existsById(idContrato)) {
            throw new EntityNotFoundException("Sem contrato com o ID informado!");
        }
        try {
            contratoRepository.deleteById(idContrato);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Não é possível deletar o contrato, pois ele está associado a outras entidades.");
        }
    }

}
