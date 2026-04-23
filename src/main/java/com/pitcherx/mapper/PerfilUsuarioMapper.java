package com.pitcherx.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.pitcherx.dto.perfilUsuario.PerfilUsuarioRequestDTO;
import com.pitcherx.dto.perfilUsuario.PerfilUsuarioResponseDTO;
import com.pitcherx.model.PerfilUsuario;

@Mapper(componentModel = "spring")
public interface PerfilUsuarioMapper {
	
	PerfilUsuarioResponseDTO toDTO(PerfilUsuario perfilUsuario);
	
	@Mapping(target = "idPerfilUsuario", ignore = true)
	PerfilUsuario toEntity(PerfilUsuarioRequestDTO perfilUsuarioRequestDTO);
	
	void updateFromDTO(PerfilUsuarioRequestDTO perfilUsuarioRequestDTO, @MappingTarget PerfilUsuario perfilUsuario);

}
