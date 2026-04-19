package com.pitcherx.service;

import com.pitcherx.dto.especialidade.EspecialidadeRequestDTO;
import com.pitcherx.dto.especialidade.EspecialidadeResponseDTO;
import com.pitcherx.model.Especialidade;
import com.pitcherx.repository.EspecialidadeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EspecialidadeService {

    private final EspecialidadeRepository especialidadeRepository;

    public EspecialidadeService(EspecialidadeRepository especialidadeRepository) {
        this.especialidadeRepository = especialidadeRepository;
    }

    @Transactional(readOnly = true)
    public List<EspecialidadeResponseDTO> listarEspecialidades(){
        return especialidadeRepository.findAll().stream()
                .map(this::toReponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public EspecialidadeResponseDTO buscarEspecialidadePorId(Long idEspecialidade){
        Especialidade especialidade = especialidadeRepository.findById(idEspecialidade)
                .orElseThrow(() -> new EntityNotFoundException("Sem especialidade com o ID informado!"));
        return toReponse(especialidade);
    }

    @Transactional
    public EspecialidadeResponseDTO criarEspecialidade(EspecialidadeRequestDTO especialidadeRequestDTO){
        if (especialidadeRepository.existsByNomeEspecialidade(especialidadeRequestDTO.nomeEspecialidade())){
            throw new IllegalArgumentException("Já existe uma especialidade com esse nome!");
        }

        Especialidade especialidade = new Especialidade();
        especialidade.setNomeEspecialidade(especialidadeRequestDTO.nomeEspecialidade());

        Especialidade salvo = especialidadeRepository.save(especialidade);
        return toReponse(salvo);
    }

    @Transactional
    public EspecialidadeResponseDTO atualizarEspecialidade(Long idEspecialidade, EspecialidadeRequestDTO especialidadeRequestDTO){
        Especialidade especialidade = especialidadeRepository.findById(idEspecialidade)
                .orElseThrow(() -> new EntityNotFoundException("Sem especialidade com o ID informado!"));

        if (especialidadeRepository.existsByNomeEspecialidadeAndIdEspecialidadeNot(especialidadeRequestDTO.nomeEspecialidade(), idEspecialidade)){
            throw new IllegalArgumentException("Já existe uma especialidade com esse nome!");
        }

        especialidade.setNomeEspecialidade(especialidadeRequestDTO.nomeEspecialidade());
        Especialidade atualizado = especialidadeRepository.save(especialidade);
        return toReponse(atualizado);
    }

    @Transactional
    public void deletarEspecialidade(Long idEspecialidade){
        if(!especialidadeRepository.existsById(idEspecialidade)){
            throw new EntityNotFoundException("Sem especialidade com o ID informado!");
        }

        try {
            especialidadeRepository.deleteById(idEspecialidade);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Não é possível deletar a especialidade, pois ela está associada a outras entidades!");
        }
    }

    public EspecialidadeResponseDTO toReponse(Especialidade especialidade){
        return new EspecialidadeResponseDTO(
                especialidade.getIdEspecialidade(),
                especialidade.getNomeEspecialidade()
        );
    }
}
