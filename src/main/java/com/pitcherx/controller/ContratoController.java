package com.pitcherx.controller;

import com.pitcherx.dto.contrato.ContratoRequestDTO;
import com.pitcherx.dto.contrato.ContratoResponseDTO;
import com.pitcherx.service.ContratoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/contrato")
@Tag(name = "Contrato", description = "API para gerenciamento de contratos")
public class ContratoController {

    private final ContratoService contratoService;

    public ContratoController(ContratoService contratoService) {
        this.contratoService = contratoService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar contratos", description = "Retorna uma lista de todos os contratos cadastrados.")
    public ResponseEntity<List<ContratoResponseDTO>> getContratos(){
        return ResponseEntity.status(HttpStatus.OK).body(contratoService.listarContratos());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Buscar contrato por ID", description = "Retorna os detalhes de um contrato específico com base no ID fornecido.")
    public ResponseEntity<ContratoResponseDTO> getContratoById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(contratoService.buscarContratoPorId(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")

    @Operation(summary = "Criar contrato", description = "Cria um novo contrato com os dados fornecidos.")
    public ResponseEntity<ContratoResponseDTO> createContrato(@Valid @RequestBody ContratoRequestDTO contratoRequestDTO){
        ContratoResponseDTO contratoResponseDTO = contratoService.criarContrato(contratoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(contratoResponseDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar contrato", description = "Atualiza os dados de um contrato específico com base no ID fornecido.")
    public ResponseEntity<ContratoResponseDTO> updateContrato(@PathVariable Long id, @Valid @RequestBody ContratoRequestDTO contratoRequestDTO){
        ContratoResponseDTO contratoResponseDTO = contratoService.atualizarContrato(id, contratoRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(contratoResponseDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletar contrato", description = "Deleta um contrato específico com base no ID fornecido.")
    public ResponseEntity<Void> deleteContrato(@PathVariable Long id) {
        contratoService.deletarContrato(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}