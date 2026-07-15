package com.pitcherx.dto.contrato;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ContratoRequestDTO(
        @NotBlank(message = "O título do contrato é obrigatório") String tituloContrato,
        @NotBlank(message = "A descrição do contrato é obrigatória") String descricaoContrato,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime dataInicioContrato,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime dataFimContrato,
        @NotNull(message = "O ID do projeto é obrigatório") Long idProjeto
) {
}
