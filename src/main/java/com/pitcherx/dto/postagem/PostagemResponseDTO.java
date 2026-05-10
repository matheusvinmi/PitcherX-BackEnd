package com.pitcherx.dto.postagem;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public record PostagemResponseDTO(
		Long idPostagem,
		String tituloPostagem,
		String textoPostagem,
		@JsonFormat(pattern = "dd/MM/yyyy")
		LocalDate dataPostagem,
		Integer likePostagem,
		Integer dislikePostagem,
		Long usuarioId
		) {

}
