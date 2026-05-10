package com.pitcherx.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.pitcherx.dto.projeto.ProjetoRequestDTO;
import com.pitcherx.dto.projeto.ProjetoResponseDTO;
import com.pitcherx.model.Projeto;

@Mapper(componentModel = "spring")
public interface ProjetoMapper {
	
	@Mapping(source = "tipoProjeto.idTipoProjeto", target = "tipoProjetoId")
	ProjetoResponseDTO toDTO(Projeto projeto);
	
	@Mapping(target = "idProjeto", ignore = true)
	Projeto toEntity(ProjetoRequestDTO projetoRequestDTO);
	
	void toUpdate(ProjetoRequestDTO projetoRequestDTO, @MappingTarget Projeto projeto);

}
