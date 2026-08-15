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
    @Operation(summary = "Curtir um conteudo", description = "Permite que um usuario curta um conteudo (postagem, comentario, subcomentario ou projeto)")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO', 'EMPRESA')")
    public ResponseEntity<Void> curtir(@PathVariable Long usuarioId,
                                        @PathVariable Long tipoConteudoId,
                                        @PathVariable Long conteudoId) {
        curtidaService.curtir(usuarioId, tipoConteudoId, conteudoId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{usuarioId}/{tipoConteudoId}/{conteudoId}")
    @Operation(summary = "Descurtir um conteudo", description = "Permite que um usuario remova sua curtida de um conteudo")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO', 'EMPRESA')")
    public ResponseEntity<Void> descurtir(@PathVariable Long usuarioId,
                                           @PathVariable Long tipoConteudoId,
                                           @PathVariable Long conteudoId) {
        curtidaService.descurtir(usuarioId, tipoConteudoId, conteudoId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/status/{usuarioId}/{tipoConteudoId}/{conteudoId}")
    @Operation(summary = "Verificar se usuario curtiu", description = "Retorna true se o usuario curtiu o conteudo, false caso contrario")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO', 'EMPRESA')")
    public ResponseEntity<Boolean> isCurtido(@PathVariable Long usuarioId,
                                              @PathVariable Long tipoConteudoId,
                                              @PathVariable Long conteudoId) {
        boolean curtido = curtidaService.isCurtido(usuarioId, tipoConteudoId, conteudoId);
        return ResponseEntity.ok(curtido);
    }

    @GetMapping("/count/{tipoConteudoId}/{conteudoId}")
    @Operation(summary = "Obter total de curtidas", description = "Retorna o numero total de curtidas de um conteudo")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO', 'EMPRESA')")
    public ResponseEntity<Long> getCurtidasCount(@PathVariable Long tipoConteudoId,
                                                  @PathVariable Long conteudoId) {
        long count = curtidaService.getCurtidasCount(tipoConteudoId, conteudoId);
        return ResponseEntity.ok(count);
    }

}
