package com.pitcherx.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.pitcherx.dto.projeto.ProjetoRequestDTO;
import com.pitcherx.dto.projeto.ProjetoResponseDTO;
import com.pitcherx.dto.riscoProjeto.RiscoProjetoResponseDTO;
import com.pitcherx.model.Projeto;
import com.pitcherx.model.RiscoProjeto;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface ProjetoMapper {
	
	@Mapping(source = "tipoProjeto.idTipoProjeto", target = "tipoProjetoId")
	@Mapping(target = "imagens", expression = "java(projeto.getImagens().isEmpty() ? (projeto.getUrlImagemProjeto() == null ? java.util.List.of() : java.util.List.of(projeto.getUrlImagemProjeto())) : projeto.getImagens().stream().map(com.pitcherx.model.ProjetoImagem::getUrlImagem).toList())")
	@Mapping(target = "riscos", source = "riscos")
	ProjetoResponseDTO toDTO(Projeto projeto);

	List<RiscoProjetoResponseDTO> toRiscoDTOs(Set<RiscoProjeto> riscos);

	RiscoProjetoResponseDTO toRiscoDTO(RiscoProjeto riscoProjeto);
	
	@Mapping(target = "idProjeto", ignore = true)
	@Mapping(target = "riscos", ignore = true)
	Projeto toEntity(ProjetoRequestDTO projetoRequestDTO);
	
	@Mapping(target = "urlImagemProjeto", ignore = true)
	@Mapping(target = "riscos", ignore = true)
	void toUpdate(ProjetoRequestDTO projetoRequestDTO, @MappingTarget Projeto projeto);

}
