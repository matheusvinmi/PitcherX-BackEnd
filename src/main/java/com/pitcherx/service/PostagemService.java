package com.pitcherx.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pitcherx.dto.postagem.PostagemRequestDTO;
import com.pitcherx.dto.postagem.PostagemResponseDTO;
import com.pitcherx.mapper.PostagemMapper;
import com.pitcherx.model.Postagem;
import com.pitcherx.model.Usuario;
import com.pitcherx.repository.PostagemRepository;
import com.pitcherx.repository.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PostagemService {

	private final PostagemRepository postagemRepository;
	private final PostagemMapper postagemMapper;
	private final UsuarioRepository usuarioRepository;
	
	public PostagemService(PostagemRepository postagemRepository, PostagemMapper postagemMapper,
			UsuarioRepository usuarioRepository) {
		this.postagemRepository = postagemRepository;
		this.postagemMapper = postagemMapper;
		this.usuarioRepository = usuarioRepository;
	}
	
	@Transactional(readOnly = true)
	public List<PostagemResponseDTO> listarPostagens(){
		return postagemRepository.findAll().stream()
				.map(postagemMapper::toDTO)
				.toList();
	}
	
	@Transactional(readOnly = true)
	public PostagemResponseDTO buscarPostagemPorId(Long idPostagem) {
		Postagem postagem = postagemRepository.findById(idPostagem)
				.orElseThrow(() -> new EntityNotFoundException("Sem postagem com o ID informado!"));
		return postagemMapper.toDTO(postagem);
	}
	
	@Transactional
	public PostagemResponseDTO criarPostagem(PostagemRequestDTO postagemRequestDTO) {
		Usuario usuario = usuarioRepository.findById(postagemRequestDTO.usuarioId())
				.orElseThrow(() -> new EntityNotFoundException("Sem usuário com o ID informado!"));
		
		Postagem postagem = postagemMapper.toEntity(postagemRequestDTO);
		postagem.setUsuario(usuario);
		
		Postagem salvo = postagemRepository.save(postagem);
		return postagemMapper.toDTO(salvo);
	}
	
	@Transactional
	public PostagemResponseDTO atualizarPostagem(Long idPostagem, PostagemRequestDTO postagemRequestDTO) {
		Postagem postagem = postagemRepository.findById(idPostagem)
				.orElseThrow(() -> new EntityNotFoundException("Sem postagem com o ID informado!"));
		Usuario usuario = usuarioRepository.findById(postagemRequestDTO.usuarioId())
				.orElseThrow(() -> new EntityNotFoundException("Sem usuário com o ID informado!"));
		
		postagemMapper.toUpdate(postagemRequestDTO, postagem);
		postagem.setUsuario(usuario);
		
		Postagem salvo = postagemRepository.save(postagem);
		return postagemMapper.toDTO(salvo);
	}
	
	public void deletarPostagem(Long idPostagem) {
		if (!postagemRepository.existsById(idPostagem)) {
			throw new EntityNotFoundException("Sem postagem com o ID informado!");
		}
		try {
			postagemRepository.deleteById(idPostagem);
		} catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Não é possível deletar a postagem, pois ela está associada a outras entidades!");
		}
	}
}
