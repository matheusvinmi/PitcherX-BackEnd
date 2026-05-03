package com.pitcherx.dto.endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EnderecoRequestDTO(
		@NotBlank(message = "O CEP é obrigatório!") String cep,
		@NotBlank(message = "A rua do endereço é obrigatório!") String ruaEndereco,
		@NotBlank(message = "O bairro do endereço é obrigatório!") String bairroEndereco,
		@NotNull(message = "O número do endereço é obrigatório!") Integer numeroEndereco,
		@NotNull(message = "O ID do usuário é obrigatório!") Long usuarioId
		) {

}