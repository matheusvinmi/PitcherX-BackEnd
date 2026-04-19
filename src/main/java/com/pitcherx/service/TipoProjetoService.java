package com.pitcherx.service;

import com.pitcherx.dto.tipoProjeto.TipoProjetoRequestDTO;
import com.pitcherx.dto.tipoProjeto.TipoProjetoResponseDTO;
import com.pitcherx.model.TipoProjeto;
import com.pitcherx.repository.TipoProjetoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TipoProjetoService {

    private final TipoProjetoRepository tipoProjetoRepository;

    public TipoProjetoService(TipoProjetoRepository tipoProjetoRepository) {
        this.tipoProjetoRepository = tipoProjetoRepository;
    }

    @Transactional(readOnly = true)
    public List<TipoProjetoResponseDTO> listarTiposProjeto(){
        return tipoProjetoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TipoProjetoResponseDTO buscarTipoProjetoPorId(Long idTipoProjeto){
        TipoProjeto tipoProjeto = tipoProjetoRepository.findById(idTipoProjeto)
                .orElseThrow(() -> new EntityNotFoundException("Sem tipo de projeto com o ID informado!"));
        return toResponse(tipoProjeto);
    }

    @Transactional
    public TipoProjetoResponseDTO criarTipoProjeto(TipoProjetoRequestDTO tipoProjetoRequestDTO){

        if (tipoProjetoRepository.existsByNomeTipoProjeto(tipoProjetoRequestDTO.nomeTipoProjeto())){
            throw new IllegalArgumentException("Já existe um tipo de projeto com esse nome!");
        }

        TipoProjeto tipoProjeto = new TipoProjeto();
        tipoProjeto.setNomeTipoProjeto(tipoProjetoRequestDTO.nomeTipoProjeto());
        tipoProjeto.setDescricaoTipoProjeto(tipoProjetoRequestDTO.descricaoTipoProjeto());

        TipoProjeto salvo = tipoProjetoRepository.save(tipoProjeto);
        return toResponse(salvo);

    }

    @Transactional
    public TipoProjetoResponseDTO atualizarTipoProjeto(Long idTipoProjeto, TipoProjetoRequestDTO tipoProjetoRequestDTO){
        TipoProjeto tipoProjeto = tipoProjetoRepository.findById(idTipoProjeto)
                .orElseThrow(() -> new EntityNotFoundException("Sem tipo de projeto com o ID informado!"));

        if (tipoProjetoRepository.existsByNomeTipoProjetoAndIdTipoProjetoNot(tipoProjetoRequestDTO.nomeTipoProjeto(), idTipoProjeto)){
            throw new IllegalArgumentException("Já existe um tipo de projeto com esse nome!");
        }

        tipoProjeto.setNomeTipoProjeto(tipoProjetoRequestDTO.nomeTipoProjeto());
        tipoProjeto.setDescricaoTipoProjeto(tipoProjetoRequestDTO.descricaoTipoProjeto());

        TipoProjeto atualizado = tipoProjetoRepository.save(tipoProjeto);
        return toResponse(atualizado);
    }

    @Transactional
    public void deletarTipoProjeto(Long idTipoProjeto){
        if (!tipoProjetoRepository.existsById(idTipoProjeto)){
            throw new EntityNotFoundException("Sem tipo de projeto com o ID informado!");
        }
        try {
            tipoProjetoRepository.deleteById(idTipoProjeto);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Não é possível deletar o tipo de projeto, pois ele está associado a uma ou mais entidades!");
        }
    }

    public TipoProjetoResponseDTO toResponse(TipoProjeto tipoProjeto){
        return new TipoProjetoResponseDTO(
                tipoProjeto.getIdTipoProjeto(),
                tipoProjeto.getNomeTipoProjeto(),
                tipoProjeto.getDescricaoTipoProjeto()
        );
    }

}
