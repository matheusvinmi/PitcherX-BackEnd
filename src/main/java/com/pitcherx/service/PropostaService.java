package com.pitcherx.service;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pitcherx.dto.proposta.PropostaRequestDTO;
import com.pitcherx.dto.proposta.PropostaResponseDTO;
import com.pitcherx.mapper.PropostaMapper;
import com.pitcherx.model.Projeto;
import com.pitcherx.model.Proposta;
import com.pitcherx.model.Contrato;
import com.pitcherx.repository.ContratoRepository;
import com.pitcherx.repository.ProjetoRepository;
import com.pitcherx.repository.PropostaRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PropostaService {

    private final PropostaRepository propostaRepository;
    private final PropostaMapper propostaMapper;
    private final ProjetoRepository projetoRepository;
    private final ContratoRepository contratoRepository;
    
    public PropostaService(PropostaRepository propostaRepository, PropostaMapper propostaMapper, ProjetoRepository projetoRepository, ContratoRepository contratoRepository) {
        this.propostaRepository = propostaRepository;
        this.propostaMapper = propostaMapper;
        this.projetoRepository = projetoRepository;
        this.contratoRepository = contratoRepository;
    }
    
    @Transactional(readOnly = true)
    public List<PropostaResponseDTO> listarPropostas(){
        return propostaRepository.findAll().stream()
                .map(propostaMapper::toDTO)
                .toList();
    }
    
    @Transactional(readOnly = true)
    public List<PropostaResponseDTO> listarPropostasByProjeto(Long projetoId){
        List<Proposta> propostas = propostaRepository.findByProjeto_IdProjeto(projetoId);
        return propostas.stream()
                .map(propostaMapper::toDTO)
                .toList();
    }
    
    @Transactional(readOnly = true)
    public PropostaResponseDTO buscarPropostaPorId(Long idProposta) {
        Proposta proposta = propostaRepository.findById(idProposta)
                .orElseThrow(() -> new EntityNotFoundException("Sem proposta com o ID informado!"));
        return propostaMapper.toDTO(proposta);
    }
    
    @Transactional
    public PropostaResponseDTO criarProposta(PropostaRequestDTO propostaRequestDTO) {
        
        Proposta proposta = propostaMapper.toEntity(propostaRequestDTO);
        
        if (propostaRequestDTO.projetoId() != null) {
            Projeto projeto = projetoRepository.findById(propostaRequestDTO.projetoId())
                    .orElseThrow(() -> new EntityNotFoundException("Sem projeto com o ID informado!"));
            proposta.setProjeto(projeto);
        }
        
        Proposta salvo = propostaRepository.save(proposta);
        return propostaMapper.toDTO(salvo);
    }
    
    @Transactional
    public PropostaResponseDTO aceitarProposta(Long idProposta) {
        Proposta proposta = propostaRepository.findById(idProposta)
                .orElseThrow(() -> new EntityNotFoundException("Sem proposta com o ID informado!"));
        
        if (!"PENDENTE".equals(proposta.getStatusProposta())) {
            throw new IllegalArgumentException("Esta proposta já foi processada!");
        }
        
        // Check if there's a pending contract linked to this proposal
        Contrato contrato = contratoRepository.findByPropostaIdAndStatusContrato(idProposta, "PENDENTE");
        if (contrato != null) {
            throw new IllegalArgumentException("Existe um contrato pendente para esta proposta. Por favor, aceite o contrato primeiro.");
        }
        
        proposta.setStatusProposta("ACEITA");
        
        Projeto projeto = proposta.getProjeto();
        if (projeto != null) {
            Double valorAtual = projeto.getValorArrecadado() != null ? projeto.getValorArrecadado() : 0.0;
            projeto.setValorArrecadado(valorAtual + proposta.getValorProposta());
            projetoRepository.save(projeto);
        }
        
        Proposta salvo = propostaRepository.save(proposta);
        return propostaMapper.toDTO(salvo);
    }
    
    @Transactional
    public PropostaResponseDTO recusarProposta(Long idProposta) {
        Proposta proposta = propostaRepository.findById(idProposta)
                .orElseThrow(() -> new EntityNotFoundException("Sem proposta com o ID informado!"));
        
        if (!"PENDENTE".equals(proposta.getStatusProposta())) {
            throw new IllegalArgumentException("Esta proposta já foi processada!");
        }
        
        proposta.setStatusProposta("RECUSADA");
        Proposta salvo = propostaRepository.save(proposta);
        return propostaMapper.toDTO(salvo);
    }
    
    @Transactional
    public void deletarProposta(Long idProposta) {
        if(!propostaRepository.existsById(idProposta)) {
            throw new EntityNotFoundException("Sem proposta com o ID informado!");
        }
        try {
            propostaRepository.deleteById(idProposta);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Não é possível deletar esta proposta, pois ela está associada a outras entidades.");
        }
    }

}
