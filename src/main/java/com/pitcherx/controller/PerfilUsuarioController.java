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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pitcherx.dto.perfilUsuario.PerfilUsuarioRequestDTO;
import com.pitcherx.dto.perfilUsuario.PerfilUsuarioResponseDTO;
import com.pitcherx.service.PerfilUsuarioService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/perfil-usuario")
public class PerfilUsuarioController {
	
	private final PerfilUsuarioService perfilUsuarioService;
	
	public PerfilUsuarioController(PerfilUsuarioService perfilUsuarioService) {
		this.perfilUsuarioService = perfilUsuarioService;
	}
	
	@GetMapping
	public ResponseEntity<List<PerfilUsuarioResponseDTO>> getPerfilUsuario(){
		return ResponseEntity.status(HttpStatus.OK).body(perfilUsuarioService.listarPerfilsUsuario());
	}
	
	@GetMapping("/{id}") 
	public ResponseEntity<PerfilUsuarioResponseDTO> getPerfilUsuarioById(@PathVariable Long id){
		return ResponseEntity.status(HttpStatus.OK).body(perfilUsuarioService.buscarPerfilUsuarioPorId(id));
	}
	
	@PostMapping
	public ResponseEntity<PerfilUsuarioResponseDTO> savePerfilUsuario(@Validated @RequestBody PerfilUsuarioRequestDTO perfilUsuarioRequestDTO){
		PerfilUsuarioResponseDTO perfilUsuarioResponseDTO = perfilUsuarioService.criarPerfilUsuario(perfilUsuarioRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(perfilUsuarioResponseDTO);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<PerfilUsuarioResponseDTO> updatePerfilUsuario(@PathVariable Long id, @Validated @RequestBody PerfilUsuarioRequestDTO perfilUsuarioRequestDTO){
		PerfilUsuarioResponseDTO perfilUsuarioResponseDTO = perfilUsuarioService.atualizarPerfilUsuario(id, perfilUsuarioRequestDTO);
		return ResponseEntity.status(HttpStatus.OK).body(perfilUsuarioResponseDTO);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletarPerfilUsuario(@PathVariable Long id){
		perfilUsuarioService.deletarPerfilUsuario(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
