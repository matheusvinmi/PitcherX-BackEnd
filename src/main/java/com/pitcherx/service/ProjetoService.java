package com.pitcherx.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pitcherx.dto.projeto.ProjetoRequestDTO;
import com.pitcherx.dto.projeto.ProjetoResponseDTO;
import com.pitcherx.mapper.ProjetoMapper;
import com.pitcherx.model.Projeto;
import com.pitcherx.model.TipoProjeto;
import com.pitcherx.repository.ProjetoRepository;
import com.pitcherx.repository.TipoProjetoRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProjetoService {
	
	private final ProjetoRepository projetoRepository;
	private final ProjetoMapper projetoMapper;
	private final TipoProjetoRepository tipoProjetoRepository;
	
	public ProjetoService(ProjetoRepository projetoRepository, ProjetoMapper projetoMapper,
			TipoProjetoRepository tipoProjetoRepository) {
		this.projetoRepository = projetoRepository;
		this.projetoMapper = projetoMapper;
		this.tipoProjetoRepository = tipoProjetoRepository;
	}
	
	@Transactional(readOnly = true)
	public List<ProjetoResponseDTO> listarProjetos(){
		return projetoRepository.findAll().stream()
				.map(projetoMapper::toDTO)
				.toList();
	}
	
	@Transactional(readOnly = true)
	public ProjetoResponseDTO buscarProjetoPorId(Long idProjeto) {
		Projeto projeto = projetoRepository.findById(idProjeto)
				.orElseThrow(() -> new EntityNotFoundException("Sem projeto com o ID informado!"));
		return projetoMapper.toDTO(projeto);
	}
	
	@Transactional
	public ProjetoResponseDTO criarProjeto(ProjetoRequestDTO projetoRequestDTO) {
		TipoProjeto tipoProjeto = tipoProjetoRepository.findById(projetoRequestDTO.tipoProjetoId())
				.orElseThrow(() -> new EntityNotFoundException("Sem tipo de projeto com o ID informado!"));
		
		Projeto projeto = projetoMapper.toEntity(projetoRequestDTO);
		projeto.setTipoProjeto(tipoProjeto);
		
		Projeto salvo = projetoRepository.save(projeto);
		return projetoMapper.toDTO(salvo);
	}
	
	@Transactional
	public ProjetoResponseDTO atualizarProjeto(Long idProjeto, ProjetoRequestDTO projetoRequestDTO) {
		Projeto projeto = projetoRepository.findById(idProjeto)
				.orElseThrow(() -> new EntityNotFoundException("Sem projeto com o ID informado!"));
		
		TipoProjeto tipoProjeto = tipoProjetoRepository.findById(projetoRequestDTO.tipoProjetoId())
				.orElseThrow(() -> new EntityNotFoundException("Sem tipo de projeto com o ID informado!"));
		
		projetoMapper.toUpdate(projetoRequestDTO, projeto);
		projeto.setTipoProjeto(tipoProjeto);
		
		Projeto salvo = projetoRepository.save(projeto);
		return projetoMapper.toDTO(salvo);
	}
	
	@Transactional
	public void deletarProjeto(Long idProjeto) {
		if(!projetoRepository.existsById(idProjeto)) {
			throw new EntityNotFoundException("Sem projeto com o ID informado!");
		}
		try {
			projetoRepository.deleteById(idProjeto);
		} catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Não é possível deletar este projeto, pois ele está associado a outras entidades.");
		}
	}

}
