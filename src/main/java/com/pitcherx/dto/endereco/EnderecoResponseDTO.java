package com.pitcherx.dto.endereco;


public record EnderecoResponseDTO(
		Long idEndereco,
		String cep,
		String ruaEndereco,
		String bairroEndereco,
		Integer numeroEndereco,
		Long usuarioId
		) {

}
