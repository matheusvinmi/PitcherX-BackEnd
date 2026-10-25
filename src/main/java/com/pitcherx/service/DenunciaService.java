package com.pitcherx.service;

import com.pitcherx.dto.denuncia.DenunciaAdminDecisionDTO;
import com.pitcherx.dto.denuncia.DenunciaRequestDTO;
import com.pitcherx.dto.denuncia.DenunciaResponseDTO;
import com.pitcherx.exceptions.BusinessException;
import com.pitcherx.mapper.DenunciaMapper;
import com.pitcherx.model.Denuncia;
import com.pitcherx.model.Usuario;
import com.pitcherx.repository.DenunciaRepository;
import com.pitcherx.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DenunciaService {

    private final DenunciaRepository denunciaRepository;
    private final UsuarioRepository usuarioRepository;
    private final DenunciaMapper denunciaMapper;

    @Transactional
    public DenunciaResponseDTO criarDenuncia(DenunciaRequestDTO requestDTO, Usuario usuarioLogado) {
        if (requestDTO.tipoDenuncia() == Denuncia.TipoDenuncia.PERFIL
                && requestDTO.alvoId().equals(usuarioLogado.getIdUsuario())) {
            throw new BusinessException("Não é possível denunciar o próprio perfil", "SELF_REPORT_NOT_ALLOWED");
        }

        Denuncia denuncia = denunciaMapper.toEntity(requestDTO);
        denuncia = denunciaRepository.save(denuncia);
        return denunciaMapper.toDTO(denuncia);
    }

    @Transactional(readOnly = true)
    public List<DenunciaResponseDTO> listarPendentes() {
        return denunciaRepository.findByStatusOrderByDataCriacaoDesc(Denuncia.StatusDenuncia.PENDENTE)
                .stream()
                .map(denunciaMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public DenunciaResponseDTO buscarPorId(Long id) {
        Denuncia denuncia = denunciaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Denúncia não encontrada", "DENUNCIA_NOT_FOUND"));
        return denunciaMapper.toDTO(denuncia);
    }

    @Transactional
    public DenunciaResponseDTO decidir(Long id, DenunciaAdminDecisionDTO decisionDTO) {
        Denuncia denuncia = denunciaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Denúncia não encontrada", "DENUNCIA_NOT_FOUND"));

        if (denuncia.getStatus() != Denuncia.StatusDenuncia.PENDENTE) {
            throw new BusinessException("Apenas denúncias pendentes podem ser decididas", "INVALID_STATUS_FOR_DECISION");
        }

        if (decisionDTO.status() != Denuncia.StatusDenuncia.ACEITA
                && decisionDTO.status() != Denuncia.StatusDenuncia.REJEITADA) {
            throw new BusinessException("Status inválido. Use ACEITA ou REJEITADA", "INVALID_DECISION_STATUS");
        }

        denuncia.setStatus(decisionDTO.status());
        denuncia.setDecisaoAdmin(decisionDTO.decisaoAdmin());
        denuncia.setDataDecisao(LocalDateTime.now());

        denuncia = denunciaRepository.save(denuncia);
        return denunciaMapper.toDTO(denuncia);
    }
}