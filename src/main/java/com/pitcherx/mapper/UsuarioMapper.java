package com.pitcherx.mapper;

import com.pitcherx.dto.nivelconta.NivelContaResponseDTO;
import com.pitcherx.dto.usuario.UsuarioRequestDTO;
import com.pitcherx.dto.usuario.UsuarioResponseDTO;
import com.pitcherx.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    @Mapping(source = "active", target = "active")
    @Mapping(target = "roles", expression = "java(usuario.getRoles().stream().map(r -> r.getNomeRole().name()).toList())")
    @Mapping(target = "nivelConta", expression = "java(usuario.getNivelConta() != null ? new com.pitcherx.dto.nivelconta.NivelContaResponseDTO(usuario.getNivelConta().getIdNivelConta(), usuario.getNivelConta().getNome().name(), usuario.getNivelConta().getDescricao(), usuario.getNivelConta().getOrdem()) : null)")
    UsuarioResponseDTO toDTO(Usuario usuario);

    @Mapping(target = "idUsuario", ignore = true)
    Usuario toEntity(UsuarioRequestDTO usuarioRequestDTO);

    void updateFromDTO(UsuarioRequestDTO usuarioRequestDTO, @MappingTarget Usuario usuario);

}
