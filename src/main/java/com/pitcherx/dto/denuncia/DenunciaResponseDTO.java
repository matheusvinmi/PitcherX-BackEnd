package com.pitcherx.dto.denuncia;

import com.pitcherx.model.Denuncia;
import io.swagger.v3.oas.annotations.media.Schema;

public record DenunciaResponseDTO(
        @Schema(description = "ID da denúncia", example = "1")
        Long idDenuncia,

        @Schema(description = "Tipo do conteúdo denunciado", example = "POSTAGEM")
        Denuncia.TipoDenuncia tipoDenuncia,

        @Schema(description = "ID do alvo denunciado", example = "1")
        Long alvoId,

        @Schema(description = "Motivo da denúncia", example = "Conteúdo impróprio")
        String motivo,

        @Schema(description = "Status da denúncia", example = "PENDENTE")
        Denuncia.StatusDenuncia status,

        @Schema(description = "Data de criação", example = "2024-01-15T10:30:00")
        java.time.LocalDateTime dataCriacao,

        @Schema(description = "Data da decisão do admin", example = "2024-01-16T14:00:00")
        java.time.LocalDateTime dataDecisao,

        @Schema(description = "Justificativa do admin", example = "Denúncia procedente, conteúdo removido")
        String decisaoAdmin
) {}