package com.pitcherx.controller;

import com.pitcherx.dto.termoPostagem.TermoPostagemRequestDTO;
import com.pitcherx.dto.termoPostagem.TermoPostagemResponseDTO;
import com.pitcherx.service.TermoPostagemService;
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
@RequestMapping("/termo-postagem")
@Tag(name = "Termo de Postagem", description = "Endpoints para gerenciamento dos termos de postagem.")
public class TermoPostagemController {

    private final TermoPostagemService termoPostagemService;

    public TermoPostagemController(TermoPostagemService termoPostagemService) {
        this.termoPostagemService = termoPostagemService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Operation(description = "Endpoint para listar os termos de postagem disponíveis.")
    public ResponseEntity<List<TermoPostagemResponseDTO>> getTermosPostagem() {
        return ResponseEntity.status(HttpStatus.OK).body(termoPostagemService.listarTermosPostagem());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    @Operation(description = "Endpoint para buscar um termo de postagem específico por ID.")
    public ResponseEntity<TermoPostagemResponseDTO> getTermoPostagemById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(termoPostagemService.buscarTermoPostagemPorId(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(description = "Endpoint para criar um novo termo de postagem.")
    public ResponseEntity<TermoPostagemResponseDTO> criarTermoPostagem(@Validated @RequestBody TermoPostagemRequestDTO termoPostagemRequestDTO){
        TermoPostagemResponseDTO termoPostagemResponseDTO = termoPostagemService.criarTermoPostagem(termoPostagemRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(termoPostagemResponseDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(description = "Endpoint para atualizar um termo de postagem existente por ID.")
    public ResponseEntity<TermoPostagemResponseDTO> atualizarTermoPostagem(@PathVariable Long id, @Validated @RequestBody TermoPostagemRequestDTO termoPostagemRequestDTO){
        TermoPostagemResponseDTO termoPostagemResponseDTO = termoPostagemService.atualizarTermoPostagem(id, termoPostagemRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(termoPostagemResponseDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(description = "Endpoint para excluir um termo de postagem por ID.")
    public ResponseEntity<Void> excluirTermoPostagem(@PathVariable Long id) {
        termoPostagemService.deletarTermoPostagem(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}