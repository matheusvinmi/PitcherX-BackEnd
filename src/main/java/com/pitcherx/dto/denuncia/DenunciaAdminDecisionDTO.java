package com.pitcherx.dto.denuncia;

import com.pitcherx.model.Denuncia;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public record DenunciaAdminDecisionDTO(
        @NotNull(message = "O status da decisão é obrigatório!")
        @Schema(description = "Nova status da denúncia", example = "ACEITA", allowableValues = {"ACEITA", "REJEITADA"})
        Denuncia.StatusDenuncia status,

        @NotBlank(message = "A justificativa da decisão é obrigatória!")
        @Schema(description = "Justificativa do admin", example = "Conteúdo violava termos de uso")
        String decisaoAdmin
) {}