package com.pitcherx.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pitcherx.dto.perfilUsuario.PerfilUsuarioRequestDTO;
import com.pitcherx.dto.perfilUsuario.PerfilUsuarioResponseDTO;
import com.pitcherx.mapper.PerfilUsuarioMapper;
import com.pitcherx.model.Especialidade;
import com.pitcherx.model.PerfilUsuario;
import com.pitcherx.repository.EspecialidadeRepository;
import com.pitcherx.repository.PerfilUsuarioRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PerfilUsuarioService {

	private final PerfilUsuarioRepository perfilUsuarioRepository;
	private final PerfilUsuarioMapper perfilUsuarioMapper;
	private final EspecialidadeRepository especialidadeRepository;
	
	public PerfilUsuarioService(PerfilUsuarioRepository perfilUsuarioRepository, PerfilUsuarioMapper perfilUsuarioMapper,
			EspecialidadeRepository especialidadeRepository) {
		this.perfilUsuarioRepository = perfilUsuarioRepository;
		this.perfilUsuarioMapper = perfilUsuarioMapper;
		this.especialidadeRepository = especialidadeRepository;
	}
	
	@Transactional(readOnly = true)
	public List<PerfilUsuarioResponseDTO> listarPerfilsUsuario(){
		return perfilUsuarioRepository.findAll().stream()
				.map(perfilUsuarioMapper::toDTO)
				.toList();
	}
	
	@Transactional(readOnly = true)
	public PerfilUsuarioResponseDTO buscarPerfilUsuarioPorId(Long idPerfilUsuario) {
		PerfilUsuario perfilUsuario = perfilUsuarioRepository.findById(idPerfilUsuario)
				.orElseThrow(() -> new EntityNotFoundException("Sem perfil de usuário com o ID informado!"));
		return perfilUsuarioMapper.toDTO(perfilUsuario);
	}
	
	@Transactional
	public PerfilUsuarioResponseDTO criarPerfilUsuario(PerfilUsuarioRequestDTO perfilUsuarioRequestDTO) {
		Especialidade especialidade = especialidadeRepository.findById(perfilUsuarioRequestDTO.idEspecialidade())
				.orElseThrow(() -> new EntityNotFoundException("Sem especialidade com o ID informado!"));
		
		PerfilUsuario perfilUsuario = perfilUsuarioMapper.toEntity(perfilUsuarioRequestDTO);
		perfilUsuario.setEspecialidade(especialidade);
		
		PerfilUsuario salvo = perfilUsuarioRepository.save(perfilUsuario);
		return perfilUsuarioMapper.toDTO(salvo);
	}
	
	@Transactional
	public PerfilUsuarioResponseDTO atualizarPerfilUsuario(Long idPerfilUsuario, PerfilUsuarioRequestDTO perfilUsuarioRequestDTO) {
		PerfilUsuario perfilUsuario = perfilUsuarioRepository.findById(idPerfilUsuario)
				.orElseThrow(() -> new EntityNotFoundException("Sem perfil de usuário com o ID informado!"));
		
		Especialidade especialidade = especialidadeRepository.findById(perfilUsuarioRequestDTO.idEspecialidade())
				.orElseThrow(() -> new EntityNotFoundException("Sem especialidade com o ID informado!"));
		
		perfilUsuarioMapper.updateFromDTO(perfilUsuarioRequestDTO, perfilUsuario);
		perfilUsuario.setEspecialidade(especialidade);
		
		PerfilUsuario salvo = perfilUsuarioRepository.save(perfilUsuario);
		return perfilUsuarioMapper.toDTO(salvo);
	}
	
	@Transactional
	public void deletarPerfilUsuario(Long idPerfilUsuario) {
		if(!perfilUsuarioRepository.existsById(idPerfilUsuario)) {
			throw new EntityNotFoundException("Sem perfil de usuário com o ID informado!");
		}
		try {
			perfilUsuarioRepository.deleteById(idPerfilUsuario);
		} catch (DataIntegrityViolationException e) {
			throw new IllegalArgumentException("Não é possível deletar este perfil de usuário, pois ele está associado a outras entidades.");
		}
	}
	
	
}
