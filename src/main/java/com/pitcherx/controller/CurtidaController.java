package com.pitcherx.controller;

import com.pitcherx.service.CurtidaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/curtida")
@Tag(name = "Curtida", description = "Endpoints relacionados a curtidas")
public class CurtidaController {

    private final CurtidaService curtidaService;

    public CurtidaController(CurtidaService curtidaService) {
        this.curtidaService = curtidaService;
    }

    @PostMapping("/{usuarioId}/{tipoConteudoId}/{conteudoId}")
    @Operation(summary = "Endpoint para curtir um conteúdo", description = "Este endpoint permite que um usuário curta um conteúdo específico, " +
            "como uma postagem, comentário ou subcomentário. O usuário deve fornecer seu ID e o ID do tipo de conteúdo que deseja curtir.")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO', 'EMPRESA')")
    public ResponseEntity<Void> saveCurtida(@PathVariable Long usuarioId,
                                                          @PathVariable Long tipoConteudoId,
                                                          @PathVariable Long conteudoId){
        curtidaService.curtirConteudo(usuarioId, tipoConteudoId, conteudoId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/remover-curtida/{id}")
    @Operation(summary = "Endpoint para remover uma curtida", description = "Este endpoint permite que um usuário remova uma curtida específica.")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO', 'EMPRESA')")
    public ResponseEntity<Void> deleteCurtida(@PathVariable Long id){
        curtidaService.deletarCurtida(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
