package com.pitcherx.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pitcherx.dto.comentario.ComentarioRequestDTO;
import com.pitcherx.dto.comentario.ComentarioResponseDTO;
import com.pitcherx.service.ComentarioService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/comentario")
@Tag(name = "Comentario", description = "Endpoints para gerenciamento de comentarios.")
public class ComentarioController {

	private final ComentarioService comentarioService;
	
	public ComentarioController(ComentarioService comentarioService) {
		this.comentarioService = comentarioService;
	}
	
	@GetMapping
	public ResponseEntity<List<ComentarioResponseDTO>> getComentarios(){
		return ResponseEntity.status(HttpStatus.OK).body(comentarioService.listarComentarios());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ComentarioResponseDTO> getComentarioById(@PathVariable Long id){
		return ResponseEntity.status(HttpStatus.OK).body(comentarioService.buscarComentarioPorId(id));
	}
	
	@PostMapping
	public ResponseEntity<ComentarioResponseDTO> saveComentario(@Validated @RequestBody ComentarioRequestDTO comentarioRequestDTO){
		ComentarioResponseDTO comentarioResponseDTO = comentarioService.criarComentario(comentarioRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(comentarioResponseDTO);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ComentarioResponseDTO> updateComentario(@PathVariable Long id, @Validated @RequestBody ComentarioRequestDTO comentarioRequestDTO){
		ComentarioResponseDTO comentarioResponseDTO = comentarioService.atualizarComentario(id, comentarioRequestDTO);
		return ResponseEntity.status(HttpStatus.OK).body(comentarioResponseDTO);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteComentario(@PathVariable Long id){
		comentarioService.deletarComentario(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	
}
