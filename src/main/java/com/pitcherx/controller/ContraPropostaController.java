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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pitcherx.dto.contraProposta.ContraPropostaRequestDTO;
import com.pitcherx.dto.contraProposta.ContraPropostaResponseDTO;
import com.pitcherx.service.ContraPropostaService;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/contra-proposta")
@Tag(name = "Contra Proposta", description = "Endpoints para gerenciamento de contra propostas")
public class ContraPropostaController {

	private final ContraPropostaService contraPropostaService;
	
	public ContraPropostaController(ContraPropostaService contraPropostaService) {
		this.contraPropostaService = contraPropostaService;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	@Operation(summary = "Listar Contra Propostas", description = "Retorna uma lista de todas as contra propostas cadastradas")
	public ResponseEntity<List<ContraPropostaResponseDTO>> getContraProposta(){
		return ResponseEntity.status(HttpStatus.OK).body(contraPropostaService.listarContraPropostas());
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{id}")
	@Operation(summary = "Buscar Contra Proposta por ID", description = "Retorna uma contra proposta específica com base no ID fornecido")
	public ResponseEntity<ContraPropostaResponseDTO> getContraPropostaById(@PathVariable Long id){
		return ResponseEntity.status(HttpStatus.OK).body(contraPropostaService.buscarContraPropostaPorId(id));
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	@Operation(summary = "Criar Contra Proposta", description = "Cria uma nova contra proposta com base nos dados fornecidos")
	public ResponseEntity<ContraPropostaResponseDTO> saveContraProposta(@Validated @RequestBody ContraPropostaRequestDTO contraPropostaRequestDTO){
		ContraPropostaResponseDTO contraPropostaResponseDTO = contraPropostaService.criarContraProposta(contraPropostaRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(contraPropostaResponseDTO);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	@Operation(summary = "Atualizar Contra Proposta", description = "Atualiza uma contra proposta existente com base no ID fornecido e nos dados fornecidos")
	public ResponseEntity<ContraPropostaResponseDTO> updateContraProposta(@PathVariable Long id, @Validated @RequestBody ContraPropostaRequestDTO contraPropostaRequestDTO){
		ContraPropostaResponseDTO contraPropostaResponseDTO = contraPropostaService.atualizarContraProposta(id, contraPropostaRequestDTO);
		return ResponseEntity.status(HttpStatus.OK).body(contraPropostaResponseDTO);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	@Operation(summary = "Deletar Contra Proposta", description = "Deleta uma contra proposta existente com base no ID fornecido")
	public ResponseEntity<Void> deleteContraProposta(@PathVariable Long id){
		contraPropostaService.deletarContraProposta(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
}