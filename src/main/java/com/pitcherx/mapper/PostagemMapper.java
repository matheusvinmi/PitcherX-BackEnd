package com.pitcherx.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.pitcherx.dto.postagem.PostagemRequestDTO;
import com.pitcherx.dto.postagem.PostagemResponseDTO;
import com.pitcherx.model.Postagem;

@Mapper(componentModel = "spring")
public interface PostagemMapper {
	
	@Mapping(source = "usuario.idUsuario", target = "usuarioId")
	PostagemResponseDTO toDTO(Postagem postagem);
	
	@Mapping(target = "idPostagem", ignore = true)
	Postagem toEntity(PostagemRequestDTO postagemRequestDTO);

	void toUpdate(PostagemRequestDTO postagemRequestDTO, @MappingTarget Postagem postagem);
}
