package com.pitcherx.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pitcherx.dto.postagem.PostagemRequestDTO;
import com.pitcherx.dto.postagem.PostagemResponseDTO;
import com.pitcherx.service.PostagemService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/postagem")
@Tag(name = "Postagem", description = "Endpoints para gerenciamento de postagens.")
public class PostagemController {

	private final PostagemService postagemService;
	
	public PostagemController(PostagemService postagemService) {
		this.postagemService = postagemService;
	}
	
	@GetMapping
	public ResponseEntity<List<PostagemResponseDTO>> getPostagens(){
		return ResponseEntity.status(HttpStatus.OK).body(postagemService.listarPostagens());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<PostagemResponseDTO> getPostagemById(@PathVariable Long id){
		return ResponseEntity.status(HttpStatus.OK).body(postagemService.buscarPostagemPorId(id));
	}
	
	@PostMapping
	public ResponseEntity<PostagemResponseDTO> savePostagem(@RequestBody PostagemRequestDTO postagemRequestDTO){
		PostagemResponseDTO postagemResponseDTO = postagemService.criarPostagem(postagemRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(postagemResponseDTO);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<PostagemResponseDTO> updatePostagem(@PathVariable Long id, @RequestBody PostagemRequestDTO postagemRequestDTO){
		PostagemResponseDTO postagemResponseDTO = postagemService.atualizarPostagem(id, postagemRequestDTO);
		return ResponseEntity.status(HttpStatus.OK).body(postagemResponseDTO);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePostagemById(@PathVariable Long id){
		postagemService.deletarPostagem(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
