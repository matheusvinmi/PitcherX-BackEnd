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

import com.pitcherx.dto.contraProposta.ContraPropostaRequestDTO;
import com.pitcherx.dto.contraProposta.ContraPropostaResponseDTO;
import com.pitcherx.service.ContraPropostaService;

@RestController
@RequestMapping("/contra-proposta")
public class ContraPropostaController {

	private final ContraPropostaService contraPropostaService;
	
	public ContraPropostaController(ContraPropostaService contraPropostaService) {
		this.contraPropostaService = contraPropostaService;
	}
	
	@GetMapping
	public ResponseEntity<List<ContraPropostaResponseDTO>> getContraProposta(){
		return ResponseEntity.status(HttpStatus.OK).body(contraPropostaService.listarContraPropostas());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ContraPropostaResponseDTO> getContraPropostaById(@PathVariable Long id){
		return ResponseEntity.status(HttpStatus.OK).body(contraPropostaService.buscarContraPropostaPorId(id));
	}
	
	@PostMapping
	public ResponseEntity<ContraPropostaResponseDTO> saveContraProposta(@Validated @RequestBody ContraPropostaRequestDTO contraPropostaRequestDTO){
		ContraPropostaResponseDTO contraPropostaResponseDTO = contraPropostaService.criarContraProposta(contraPropostaRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(contraPropostaResponseDTO);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ContraPropostaResponseDTO> updateContraProposta(@PathVariable Long id, @Validated @RequestBody ContraPropostaRequestDTO contraPropostaRequestDTO){
		ContraPropostaResponseDTO contraPropostaResponseDTO = contraPropostaService.atualizarContraProposta(id, contraPropostaRequestDTO);
		return ResponseEntity.status(HttpStatus.OK).body(contraPropostaResponseDTO);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteContraProposta(@PathVariable Long id){
		contraPropostaService.deletarContraProposta(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
}
