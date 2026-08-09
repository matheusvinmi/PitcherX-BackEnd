package com.pitcherx.service;

import com.pitcherx.model.Curtida;
import com.pitcherx.model.TipoConteudo;
import com.pitcherx.model.Usuario;
import com.pitcherx.repository.CurtidaRepository;
import com.pitcherx.repository.TipoConteudoRepository;
import com.pitcherx.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CurtidaService {

    private final CurtidaRepository curtidaRepository;
    private final UsuarioRepository usuarioRepository;
    private final TipoConteudoRepository tipoConteudoRepository;

    public CurtidaService(CurtidaRepository curtidaRepository,
                          UsuarioRepository usuarioRepository, TipoConteudoRepository tipoConteudoRepository){
        this.curtidaRepository = curtidaRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoConteudoRepository = tipoConteudoRepository;
    }

    @Transactional
    public void curtirConteudo(Long usuarioId, Long tipoConteudoId, Long conteudoId){

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Sem usuario com o ID informado!"));

        TipoConteudo tipoConteudo = tipoConteudoRepository.findById(tipoConteudoId)
                .orElseThrow(() -> new RuntimeException("Sem tipo de conteudo com o ID informado!"));

        Curtida curtida = new Curtida();

        curtida.setUsuario(usuario);
        curtida.setTipoConteudo(tipoConteudo);
        curtida.setConteudoId(conteudoId);
        curtida.setDataCurtida(LocalDateTime.now());

        curtidaRepository.save(curtida);
    }

    //metodo de remoção de curtida básico, vou fazer um com verificação de usuário, tipo de conteudo e conteudo depois
    @Transactional
    public void deletarCurtida(Long idCurtida){
        if(!curtidaRepository.existsById(idCurtida)){
            throw new RuntimeException("Sem curtida com o ID informado!");
        }
        try {
            curtidaRepository.deleteById(idCurtida);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Não é possível deletar a curtida, pois ela está associada a outras entidades.!");
        }

    }


}
