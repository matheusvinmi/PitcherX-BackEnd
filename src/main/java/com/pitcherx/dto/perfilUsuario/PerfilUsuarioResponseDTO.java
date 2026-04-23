package com.pitcherx.dto.perfilUsuario;

import com.pitcherx.model.Especialidade;

public record PerfilUsuarioResponseDTO(
		Long idPerfilUsuario,
		String linkedin,
		Especialidade especialidade
		) {

}
