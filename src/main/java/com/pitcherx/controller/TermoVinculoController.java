package com.pitcherx.controller;

import com.pitcherx.dto.termoVinculo.TermoVinculoRequestDTO;
import com.pitcherx.dto.termoVinculo.TermoVinculoResponseDTO;
import com.pitcherx.service.TermoVinculoService;
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
@RequestMapping("/termo-vinculo")
@Tag(name = "Termo de vínculo", description = "Endpoints para gerenciamento dos termos de vinculos.")
public class TermoVinculoController {

    private final TermoVinculoService termoVinculoService;

    public TermoVinculoController(TermoVinculoService termoVinculoService) {
        this.termoVinculoService = termoVinculoService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Operation(description = "Este endpoint é responsável por listar os termos de vínculo disponíveis.")
    public ResponseEntity<List<TermoVinculoResponseDTO>> getTermosVinculo() {
        return ResponseEntity.status(HttpStatus.OK).body(termoVinculoService.listarTermosVinculo());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    @Operation(description = "Este endpoint é responsável por buscar um termo de vínculo específico por ID.")
    public ResponseEntity<TermoVinculoResponseDTO> getTermoVinculoById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(termoVinculoService.buscarTermoVinculoPorId(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(description = "Este endpoint é responsável por criar um novo termo de vínculo.")
    public ResponseEntity<TermoVinculoResponseDTO> criarTermoVinculo(@Validated @RequestBody TermoVinculoRequestDTO termoVinculoRequestDTO){
        TermoVinculoResponseDTO termoVinculoResponseDTO = termoVinculoService.criarTermoVinculo(termoVinculoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(termoVinculoResponseDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(description = "Este endpoint é responsável por atualizar um termo de vínculo existente por ID.")
    public ResponseEntity<TermoVinculoResponseDTO> atualizarTermoVinculo(@PathVariable Long id, @Validated @RequestBody TermoVinculoRequestDTO termoVinculoRequestDTO) {
        TermoVinculoResponseDTO termoVinculoResponseDTO = termoVinculoService.atualizarTermoVinculo(id, termoVinculoRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(termoVinculoResponseDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(description = "Este endpoint é responsável por excluir um termo de vínculo por ID.")
    public ResponseEntity<Void> excluirTermoVinculo(@PathVariable Long id) {
        termoVinculoService.deletarTermoVinculo(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}