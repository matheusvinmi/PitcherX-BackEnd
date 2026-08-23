package com.pitcherx.dto.usuario;

public record UsuarioResponseDTO(
        Long idUsuario,
        String nomeUsuario,
        String emailUsuario,
        String telefoneUsuario,
        Boolean active, 
        String role
) {
}
