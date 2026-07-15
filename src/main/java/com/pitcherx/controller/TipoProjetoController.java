package com.pitcherx.controller;

import com.pitcherx.dto.tipoProjeto.TipoProjetoRequestDTO;
import com.pitcherx.dto.tipoProjeto.TipoProjetoResponseDTO;
import com.pitcherx.service.TipoProjetoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/tipo-projeto")
@Tag(name = "Tipo de projeto", description = "Endpoints para gerenciamento de tipos de projeto")
public class TipoProjetoController {

    private final TipoProjetoService tipoProjetoService;

    public TipoProjetoController(TipoProjetoService tipoProjetoService) {
        this.tipoProjetoService = tipoProjetoService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Operation(description = "Endpoint para listar os tipos de projeto disponíveis")
    public ResponseEntity<List<TipoProjetoResponseDTO>> getTiposProjeto() {
        return ResponseEntity.status(HttpStatus.OK).body(tipoProjetoService.listarTiposProjeto());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    @Operation(description = "Endpoint para obter um tipo de projeto específico por ID")
    public ResponseEntity<TipoProjetoResponseDTO> getTipoProjetoById(Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(tipoProjetoService.buscarTipoProjetoPorId(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(description = "Endpoint para criar um novo tipo de projeto")
    public ResponseEntity<TipoProjetoResponseDTO> createTipoProjeto(@Validated @RequestBody TipoProjetoRequestDTO tipoProjetoRequestDTO) {
        TipoProjetoResponseDTO tipoProjetoResponseDTO = tipoProjetoService.criarTipoProjeto(tipoProjetoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoProjetoResponseDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(description = "Endpoint para atualizar um tipo de projeto existente")
    public ResponseEntity<TipoProjetoResponseDTO> updateTipoProjeto(@PathVariable Long id, @Validated @RequestBody TipoProjetoRequestDTO tipoProjetoRequestDTO) {
        TipoProjetoResponseDTO tipoProjetoResponseDTO = tipoProjetoService.atualizarTipoProjeto(id, tipoProjetoRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(tipoProjetoResponseDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    @Operation(description = "Endpoint para excluir um tipo de projeto por ID")
    public ResponseEntity<Void> deleteTipoProjeto(@PathVariable Long id) {
        tipoProjetoService.deletarTipoProjeto(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}