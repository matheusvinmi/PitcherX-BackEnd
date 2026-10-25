package com.pitcherx.dto.denuncia;

import com.pitcherx.model.Denuncia;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public record DenunciaRequestDTO(
        @NotNull(message = "O tipo de denúncia é obrigatório!")
        @Schema(description = "Tipo do conteúdo denunciado", example = "POSTAGEM", allowableValues = {"PERFIL", "POSTAGEM", "PROJETO", "COMENTARIO"})
        Denuncia.TipoDenuncia tipoDenuncia,

        @NotNull(message = "O ID do alvo é obrigatório!")
        @Schema(description = "ID do perfil/postagem/projeto/comentário denunciado", example = "1")
        Long alvoId,

        @NotBlank(message = "O motivo é obrigatório!")
        @Schema(description = "Motivo da denúncia", example = "Conteúdo impróprio")
        String motivo
) {}