package com.pitcherx.dto.projeto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pitcherx.dto.riscoProjeto.RiscoProjetoResponseDTO;

public record ProjetoResponseDTO(
		Long idProjeto,
		String nomeProjeto,
		String descricaoProjeto,
		
		@JsonFormat(pattern = "dd/MM/yyyy")
		LocalDate dataInicioProjeto,
		
		@JsonFormat(pattern = "dd/MM/yyyy")
		LocalDate dataFimProjeto,
		Long tipoProjetoId,
		Boolean active,
		String urlImagemProjeto,
		List<String> imagens,
		Double metaFinanceira,
		Double valorArrecadado,
		List<RiscoProjetoResponseDTO> riscos
		) {

}
