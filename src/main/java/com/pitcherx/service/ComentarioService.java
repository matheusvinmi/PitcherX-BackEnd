package com.pitcherx.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pitcherx.dto.comentario.ComentarioRequestDTO;
import com.pitcherx.dto.comentario.ComentarioResponseDTO;
import com.pitcherx.mapper.ComentarioMapper;
import com.pitcherx.model.Comentario;
import com.pitcherx.repository.ComentarioRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ComentarioService {

	private final ComentarioRepository comentarioRepository;
	private final ComentarioMapper comentarioMapper;
	
	public ComentarioService(ComentarioRepository comentarioRepository, ComentarioMapper comentarioMapper) {
		this.comentarioRepository = comentarioRepository;
		this.comentarioMapper = comentarioMapper;
	}
	
	@Transactional(readOnly = true)
	public List<ComentarioResponseDTO> listarComentarios(){
		return comentarioRepository.findAll().stream()
				.map(comentarioMapper::toDTO)
				.toList();
	}
	
	@Transactional(readOnly = true)
	public ComentarioResponseDTO buscarComentarioPorId(Long idComentario) {
		Comentario comentario = comentarioRepository.findById(idComentario)
				.orElseThrow(() -> new EntityNotFoundException("Sem comentário com o ID informado!"));
		return comentarioMapper.toDTO(comentario);
	}
	
	@Transactional
	public ComentarioResponseDTO criarComentario(ComentarioRequestDTO comentarioRequestDTO) {
		
		Comentario comentario = comentarioMapper.toEntity(comentarioRequestDTO);
		
		Comentario salvo = comentarioRepository.save(comentario);
		return comentarioMapper.toDTO(salvo);		
	}
	
	@Transactional
	public ComentarioResponseDTO atualizarComentario(Long idComentario, ComentarioRequestDTO comentarioRequestDTO) {
		Comentario comentario = comentarioRepository.findById(idComentario)
				.orElseThrow(() -> new EntityNotFoundException("Sem comentário com o ID informado!"));
		
		comentarioMapper.toUpdate(comentarioRequestDTO, comentario);
		
		Comentario salvo = comentarioRepository.save(comentario);
		return comentarioMapper.toDTO(salvo);		
	}
	
	@Transactional
	public void deletarComentario(Long idComentario) {
		if (!comentarioRepository.existsById(idComentario)) {
			throw new EntityNotFoundException("Sem comentario com o ID informado!");
		}
		try {
			comentarioRepository.deleteById(idComentario);
		} catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Não é possível deletar o comentario, pois ele está associada a outras entidades.!");
		}
	}
	
}
