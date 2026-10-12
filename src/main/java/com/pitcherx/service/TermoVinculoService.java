package com.pitcherx.service;

import com.pitcherx.dto.termoVinculo.TermoVinculoRequestDTO;
import com.pitcherx.dto.termoVinculo.TermoVinculoResponseDTO;
import com.pitcherx.mapper.TermoVinculoMapper;
import com.pitcherx.model.Projeto;
import com.pitcherx.model.TermoVinculo;
import com.pitcherx.repository.ProjetoRepository;
import com.pitcherx.repository.TermoVinculoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TermoVinculoService {

    private final TermoVinculoRepository termoVinculoRepository;
    private final TermoVinculoMapper termoVinculoMapper;
    private final ProjetoRepository projetoRepository;

    public TermoVinculoService(TermoVinculoRepository termoVinculoRepository,
                                 TermoVinculoMapper termoVinculoMapper, ProjetoRepository projetoRepository) {
        this.termoVinculoRepository = termoVinculoRepository;
        this.termoVinculoMapper = termoVinculoMapper;
        this.projetoRepository = projetoRepository;
    }

    @Transactional(readOnly = true)
    public List<TermoVinculoResponseDTO> listarTermosVinculo() {
        return termoVinculoRepository.findAll().stream()
                .map(termoVinculoMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public TermoVinculoResponseDTO buscarTermoVinculoPorId(Long id) {
        TermoVinculo termoVinculo = termoVinculoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sem termo de vínculo com o ID informado!"));
        return termoVinculoMapper.toDTO(termoVinculo);
    }

    @Transactional(readOnly = true)
    public List<TermoVinculoResponseDTO> listarTermosPorProjeto(Long projetoId) {
        if (!projetoRepository.existsById(projetoId)) {
            throw new EntityNotFoundException("Sem projeto com o ID informado!");
        }
        return termoVinculoRepository.findByProjeto_IdProjeto(projetoId).stream()
                .map(termoVinculoMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TermoVinculoResponseDTO> listarTermosObrigatoriosPorProjeto(Long projetoId) {
        if (!projetoRepository.existsById(projetoId)) {
            throw new EntityNotFoundException("Sem projeto com o ID informado!");
        }
        return termoVinculoRepository.findByProjeto_IdProjetoAndObrigatorioTrue(projetoId).stream()
                .map(termoVinculoMapper::toDTO)
                .toList();
    }

    @Transactional
    public TermoVinculoResponseDTO criarTermoVinculo(Long projetoId, TermoVinculoRequestDTO termoVinculoRequestDTO) {
        Projeto projeto = projetoRepository.findById(projetoId)
                .orElseThrow(() -> new EntityNotFoundException("Sem projeto com o ID informado!"));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = auth.getName();

        // Verificar se o usuário é o criador do projeto
        // TODO: Implementar verificação baseada no nome do usuário logado vs criador do projeto
        // Por enquanto, apenas verificar se o termo já existe

        if (termoVinculoRepository.existsByTituloTermoVinculoAndProjeto_IdProjeto(
                termoVinculoRequestDTO.tituloTermoVinculo(), projetoId)) {
            throw new IllegalArgumentException("Já existe um termo de vínculo com esse título para este projeto!");
        }

        TermoVinculo termoVinculo = termoVinculoMapper.toEntity(termoVinculoRequestDTO);
        termoVinculo.setProjeto(projeto);
        termoVinculo.setObrigatorio(termoVinculoRequestDTO.obrigatorio() != null && termoVinculoRequestDTO.obrigatorio());

        TermoVinculo salvo = termoVinculoRepository.save(termoVinculo);
        return termoVinculoMapper.toDTO(salvo);
    }

    @Transactional
    public TermoVinculoResponseDTO atualizarTermoVinculo(Long idTermoVinculo, TermoVinculoRequestDTO termoVinculoRequestDTO) {
        TermoVinculo termoVinculo = termoVinculoRepository.findById(idTermoVinculo)
                .orElseThrow(() -> new EntityNotFoundException("Sem termo de vínculo com o ID informado!"));

        if (termoVinculoRepository.existsByTituloTermoVinculoAndIdTermoVinculoNot(
                termoVinculoRequestDTO.tituloTermoVinculo(), idTermoVinculo)) {
            throw new IllegalArgumentException("Já existe um termo de vínculo com esse título!");
        }

        termoVinculoMapper.updateFromDTO(termoVinculoRequestDTO, termoVinculo);
        TermoVinculo salvo = termoVinculoRepository.save(termoVinculo);
        return termoVinculoMapper.toDTO(salvo);
    }

    @Transactional
    public void deletarTermoVinculo(Long idTermoVinculo) {
        if (!termoVinculoRepository.existsById(idTermoVinculo)) {
            throw new EntityNotFoundException("Sem termo de vínculo com o ID informado!");
        }
        try {
            termoVinculoRepository.deleteById(idTermoVinculo);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Não é possível deletar o termo de vínculo, pois ele está associado a outras entidades!");
        }
    }
}