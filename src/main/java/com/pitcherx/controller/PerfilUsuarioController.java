package com.pitcherx.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Perfil de usuário", description = "Endpoints para gerenciamento de pefil de usuario.")
public class PerfilUsuarioController {
	
	private final PerfilUsuarioService perfilUsuarioService;
	
	public PerfilUsuarioController(PerfilUsuarioService perfilUsuarioService) {
		this.perfilUsuarioService = perfilUsuarioService;
	}
	
	@GetMapping
	@Operation(summary = "Listar Perfis de usuário", description = "Este endpoint faz a listagem de todos os perfis de usuario.")
	public ResponseEntity<List<PerfilUsuarioResponseDTO>> getPerfilUsuario(){
		return ResponseEntity.status(HttpStatus.OK).body(perfilUsuarioService.listarPerfilsUsuario());
	}
	
	@GetMapping("/{id}")
	@Operation(description = "Este endpoint faz a busca de perfil de usuario através do ID.")
	public ResponseEntity<PerfilUsuarioResponseDTO> getPerfilUsuarioById(@PathVariable Long id){
		return ResponseEntity.status(HttpStatus.OK).body(perfilUsuarioService.buscarPerfilUsuarioPorId(id));
	}
	
	@PostMapping
	@Operation(description = "Este endpoint faz o cadastro de perfil de usuario.")
	public ResponseEntity<PerfilUsuarioResponseDTO> savePerfilUsuario(@Validated @RequestBody PerfilUsuarioRequestDTO perfilUsuarioRequestDTO){
		PerfilUsuarioResponseDTO perfilUsuarioResponseDTO = perfilUsuarioService.criarPerfilUsuario(perfilUsuarioRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(perfilUsuarioResponseDTO);
	}
	
	@PutMapping("/{id}")
	@Operation(description = "Este endpoint faz a atualização de perfil de usuario através do ID.")
	public ResponseEntity<PerfilUsuarioResponseDTO> updatePerfilUsuario(@PathVariable Long id, @Validated @RequestBody PerfilUsuarioRequestDTO perfilUsuarioRequestDTO){
		PerfilUsuarioResponseDTO perfilUsuarioResponseDTO = perfilUsuarioService.atualizarPerfilUsuario(id, perfilUsuarioRequestDTO);
		return ResponseEntity.status(HttpStatus.OK).body(perfilUsuarioResponseDTO);
	}

	@DeleteMapping("/{id}")
	@Operation(description = "Este endpoint faz a remoção de perfil de usuario através do ID.")
	public ResponseEntity<Void> deletarPerfilUsuario(@PathVariable Long id){
		perfilUsuarioService.deletarPerfilUsuario(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
