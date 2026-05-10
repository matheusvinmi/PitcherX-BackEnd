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

import com.pitcherx.dto.projeto.ProjetoRequestDTO;
import com.pitcherx.dto.projeto.ProjetoResponseDTO;
import com.pitcherx.service.ProjetoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/projeto")
@Tag(name = "Projeto", description = "Endpoints para gerenciamento de projetos")
public class ProjetoController {

	private final ProjetoService projetoService;
	
	public ProjetoController(ProjetoService projetoService) {
		this.projetoService = projetoService;
	}
	
	@GetMapping
    @Operation(description = "Endpoint para listar os projetos publicados")
	public ResponseEntity<List<ProjetoResponseDTO>> getProjetos(){
		return ResponseEntity.status(HttpStatus.OK).body(projetoService.listarProjetos());
	}
	
	@GetMapping("/{id}")
    @Operation(description = "Endpoint para obter um projeto específico por ID")
	public ResponseEntity<ProjetoResponseDTO> getProjetoById(@PathVariable Long id){
		return ResponseEntity.status(HttpStatus.OK).body(projetoService.buscarProjetoPorId(id));
	}
	
	@PostMapping
    @Operation(description = "Endpoint para criar um novo projeto")
	public ResponseEntity<ProjetoResponseDTO> createProjeto(@Validated @RequestBody ProjetoRequestDTO projetoRequestDTO){
		ProjetoResponseDTO projetoResponseDTO = projetoService.criarProjeto(projetoRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(projetoResponseDTO);
	}
	
	@PutMapping("/{id}")
    @Operation(description = "Endpoint para atualizar um projeto")
	public ResponseEntity<ProjetoResponseDTO> updateProjeto(@PathVariable Long id, @Validated @RequestBody ProjetoRequestDTO projetoRequestDTO){
		ProjetoResponseDTO projetoResponseDTO = projetoService.atualizarProjeto(id, projetoRequestDTO);
		return ResponseEntity.status(HttpStatus.OK).body(projetoResponseDTO);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProjeto(@PathVariable Long id){
		projetoService.deletarProjeto(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
}
