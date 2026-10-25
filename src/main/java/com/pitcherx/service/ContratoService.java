package com.pitcherx.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pitcherx.dto.contrato.ContratoRequestDTO;
import com.pitcherx.dto.contrato.ContratoResponseDTO;
import com.pitcherx.mapper.ContratoMapper;
import com.pitcherx.model.Contrato;
import com.pitcherx.model.Projeto;
import com.pitcherx.model.Proposta;
import com.pitcherx.repository.ContratoRepository;
import com.pitcherx.repository.ProjetoRepository;
import com.pitcherx.repository.PropostaRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ContratoService {

    private final ContratoRepository contratoRepository;
    private final ContratoMapper contratoMapper;
    private final ProjetoRepository projetoRepository;
    private final PropostaRepository propostaRepository;

    public ContratoService(ContratoRepository contratoRepository, ContratoMapper contratoMapper,
            ProjetoRepository projetoRepository, PropostaRepository propostaRepository) {
        this.contratoRepository = contratoRepository;
        this.contratoMapper = contratoMapper;
        this.projetoRepository = projetoRepository;
        this.propostaRepository = propostaRepository;
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
    public ContratoResponseDTO criarContrato(ContratoRequestDTO contratoRequestDTO) {
        Projeto projeto = projetoRepository.findById(contratoRequestDTO.projetoId())
                .orElseThrow(() -> new EntityNotFoundException("Sem projeto com o ID informado!"));

        Contrato contrato = contratoMapper.toEntity(contratoRequestDTO);
        contrato.setProjeto(projeto);
        if (contratoRequestDTO.propostaId() != null) {
            Proposta proposta = propostaRepository.findById(contratoRequestDTO.propostaId())
                    .orElseThrow(() -> new EntityNotFoundException("Sem proposta com o ID informado!"));
            contrato.setProposta(proposta);
            contrato.setStatusContrato("PENDENTE");
        }
        contrato.setActive(true);

        Contrato salvo = contratoRepository.save(contrato);
        return contratoMapper.toDTO(salvo);
    }

    @Transactional
    public ContratoResponseDTO atualizarContrato(Long idContrato, ContratoRequestDTO contratoRequestDTO) {
        Contrato contrato = contratoRepository.findById(idContrato)
                .orElseThrow(() -> new EntityNotFoundException("Sem contrato com o ID informado!"));

        Projeto projeto = projetoRepository.findById(contratoRequestDTO.projetoId())
                .orElseThrow(() -> new EntityNotFoundException("Sem projeto com o ID informado!"));

        contratoMapper.toUpdate(contratoRequestDTO, contrato);
        contrato.setProjeto(projeto);

        Contrato salvo = contratoRepository.save(contrato);
        return contratoMapper.toDTO(salvo);
    }

    @Transactional
    public void deletarContrato(Long idContrato) {
        if(!contratoRepository.existsById(idContrato)) {
            throw new EntityNotFoundException("Sem contrato com o ID informado!");
        }
        try {
            contratoRepository.deleteById(idContrato);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Não é possível deletar este contrato, pois ele está associado a outras entidades.");
        }
    }

    @Transactional
    public ContratoResponseDTO aceitarContrato(Long idContrato) {
        Contrato contrato = contratoRepository.findById(idContrato)
                .orElseThrow(() -> new EntityNotFoundException("Sem contrato com o ID informado!"));

        if (!"PENDENTE".equals(contrato.getStatusContrato())) {
            throw new IllegalArgumentException("Este contrato já foi processado!");
        }

        contrato.setStatusContrato("ACEITE");

        if (contrato.getProposta() != null) {
            Proposta proposta = contrato.getProposta();
            Projeto projeto = proposta.getProjeto();
            if (projeto != null) {
                Double valorAtual = projeto.getValorArrecadado() != null ? projeto.getValorArrecadado() : 0.0;
                projeto.setValorArrecadado(valorAtual + proposta.getValorProposta());
                projetoRepository.save(projeto);
            }
        }

        Contrato salvo = contratoRepository.save(contrato);
        return contratoMapper.toDTO(salvo);
    }

}
