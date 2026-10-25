package com.pitcherx.controller;

import com.pitcherx.dto.denuncia.DenunciaAdminDecisionDTO;
import com.pitcherx.dto.denuncia.DenunciaRequestDTO;
import com.pitcherx.dto.denuncia.DenunciaResponseDTO;
import com.pitcherx.model.Denuncia;
import com.pitcherx.model.Usuario;
import com.pitcherx.service.DenunciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/denuncia")
@RequiredArgsConstructor
@Tag(name = "Denúncias", description = "Endpoints para denúncias anônimas de perfis, postagens, projetos e comentários")
@SecurityRequirement(name = "bearerAuth")
public class DenunciaController {

    private final DenunciaService denunciaService;

    @PostMapping
    @PreAuthorize("hasRole('USUARIO')")
    @Operation(summary = "Criar denúncia anônima (requer autenticação para validação de auto-denúncia)")
    public ResponseEntity<DenunciaResponseDTO> criarDenuncia(
            @Valid @RequestBody DenunciaRequestDTO requestDTO,
            @AuthenticationPrincipal Usuario usuarioLogado) {
        DenunciaResponseDTO response = denunciaService.criarDenuncia(requestDTO, usuarioLogado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar denúncias pendentes (apenas admin)")
    public ResponseEntity<List<DenunciaResponseDTO>> listarPendentes() {
        return ResponseEntity.ok(denunciaService.listarPendentes());
    }

    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Buscar denúncia por ID (apenas admin)")
    public ResponseEntity<DenunciaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(denunciaService.buscarPorId(id));
    }

    @PutMapping("/admin/{id}/decisao")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Decidir sobre denúncia (apenas admin)")
    public ResponseEntity<DenunciaResponseDTO> decidir(
            @PathVariable Long id,
            @Valid @RequestBody DenunciaAdminDecisionDTO decisionDTO) {
        return ResponseEntity.ok(denunciaService.decidir(id, decisionDTO));
    }
}