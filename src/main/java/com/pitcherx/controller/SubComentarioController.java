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

import com.pitcherx.dto.subComentario.SubComentarioRequestDTO;
import com.pitcherx.dto.subComentario.SubComentarioResponseDTO;
import com.pitcherx.service.SubComentarioService;

@RestController
@RequestMapping("/sub-comentario")
public class SubComentarioController {

	private final SubComentarioService subComentarioService;
	
	public SubComentarioController(SubComentarioService subComentarioService) {
		this.subComentarioService = subComentarioService;
	}
	
	@GetMapping
	public ResponseEntity<List<SubComentarioResponseDTO>> getSubComentarios(){
		return ResponseEntity.status(HttpStatus.OK).body(subComentarioService.listarSubComentarios());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<SubComentarioResponseDTO> getSubComentarioById(@PathVariable Long id){
		return ResponseEntity.status(HttpStatus.OK).body(subComentarioService.buscarSubComentarioPorId(id));
	}
	
	@PostMapping
	public ResponseEntity<SubComentarioResponseDTO> saveSubComentario(@Validated @RequestBody SubComentarioRequestDTO subComentarioRequestDTO){
		SubComentarioResponseDTO subComentarioResponseDTO = subComentarioService.criarSubComentario(subComentarioRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(subComentarioResponseDTO);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<SubComentarioResponseDTO> updateSubComentario(@PathVariable Long id, @Validated @RequestBody SubComentarioRequestDTO subComentarioRequestDTO){
		SubComentarioResponseDTO subComentarioResponseDTO = subComentarioService.atualizarSubComentario(id, subComentarioRequestDTO);
		return ResponseEntity.status(HttpStatus.OK).body(subComentarioResponseDTO);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteSubComentario(@PathVariable Long id){
		subComentarioService.deletarSubComentario(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	
}
