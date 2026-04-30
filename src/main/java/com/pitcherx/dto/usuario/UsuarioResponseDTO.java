package com.pitcherx.dto.usuario;

public record UsuarioResponseDTO(
        Long idUsuario,
        String nomeUsuario,
        String emailUsuario,
        String telefoneUsuario,
        String urlImagemUsuario,
        Boolean active, 
        String role
) {
}
