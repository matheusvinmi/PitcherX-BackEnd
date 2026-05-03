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

import com.pitcherx.dto.endereco.EnderecoRequestDTO;
import com.pitcherx.dto.endereco.EnderecoResponseDTO;
import com.pitcherx.service.EnderecoService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/endereco")
@Tag(name = "Endereco", description = "Endpoints para gerenciamento de endereços.")
public class EnderecoController {

	private final EnderecoService enderecoService;
	
	public EnderecoController(EnderecoService enderecoService) {
		this.enderecoService = enderecoService;
	}
	
	@GetMapping
	public ResponseEntity<List<EnderecoResponseDTO>> getEnderecos(){
		return ResponseEntity.status(HttpStatus.OK).body(enderecoService.listarEndereco());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<EnderecoResponseDTO> getEnderecoById(@PathVariable Long id){
		return ResponseEntity.status(HttpStatus.OK).body(enderecoService.buscarEnderecoPorId(id));
	}
	
	@PostMapping
	public ResponseEntity<EnderecoResponseDTO> createEndereco(@Valid @RequestBody EnderecoRequestDTO enderecoRequestDTO){
		EnderecoResponseDTO enderecoResponseDTO = enderecoService.criarEndereco(enderecoRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(enderecoResponseDTO);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<EnderecoResponseDTO> updateEndereco(@PathVariable Long id, @Valid @RequestBody EnderecoRequestDTO enderecoRequestDTO){
		EnderecoResponseDTO enderecoResponseDTO = enderecoService.atualizarEndereco(id, enderecoRequestDTO);
		return ResponseEntity.status(HttpStatus.OK).body(enderecoResponseDTO);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteEndereco(@PathVariable Long id){
		enderecoService.deletarEndereco(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
}
