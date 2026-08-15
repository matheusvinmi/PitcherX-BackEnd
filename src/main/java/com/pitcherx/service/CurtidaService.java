package com.pitcherx.service;

import com.pitcherx.model.Curtida;
import com.pitcherx.model.Postagem;
import com.pitcherx.model.Comentario;
import com.pitcherx.model.SubComentario;
import com.pitcherx.model.Projeto;
import com.pitcherx.model.TipoConteudo;
import com.pitcherx.model.TipoConteudoEnum;
import com.pitcherx.model.Usuario;
import com.pitcherx.repository.CurtidaRepository;
import com.pitcherx.repository.PostagemRepository;
import com.pitcherx.repository.ComentarioRepository;
import com.pitcherx.repository.SubComentarioRepository;
import com.pitcherx.repository.ProjetoRepository;
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
    private final PostagemRepository postagemRepository;
    private final ComentarioRepository comentarioRepository;
    private final SubComentarioRepository subComentarioRepository;
    private final ProjetoRepository projetoRepository;

    public CurtidaService(CurtidaRepository curtidaRepository,
                          UsuarioRepository usuarioRepository,
                          TipoConteudoRepository tipoConteudoRepository,
                          PostagemRepository postagemRepository,
                          ComentarioRepository comentarioRepository,
                          SubComentarioRepository subComentarioRepository,
                          ProjetoRepository projetoRepository) {
        this.curtidaRepository = curtidaRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoConteudoRepository = tipoConteudoRepository;
        this.postagemRepository = postagemRepository;
        this.comentarioRepository = comentarioRepository;
        this.subComentarioRepository = subComentarioRepository;
        this.projetoRepository = projetoRepository;
    }

    @Transactional
    public void curtir(Long usuarioId, Long tipoConteudoId, Long conteudoId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Sem usuario com o ID informado!"));

        TipoConteudo tipoConteudo = tipoConteudoRepository.findById(tipoConteudoId)
                .orElseThrow(() -> new RuntimeException("Sem tipo de conteudo com o ID informado!"));

        validarConteudoExiste(tipoConteudo.getNomeTipoConteudo(), conteudoId);

        if (curtidaRepository.existsByUsuarioIdAndTipoConteudoIdAndConteudoId(usuarioId, tipoConteudoId, conteudoId)) {
            throw new RuntimeException("Usuario ja curtiu este conteudo!");
        }

        Curtida curtida = new Curtida();
        curtida.setUsuario(usuario);
        curtida.setTipoConteudo(tipoConteudo);
        curtida.setConteudoId(conteudoId);
        curtida.setDataCurtida(LocalDateTime.now());

        curtidaRepository.save(curtida);
    }

    @Transactional
    public void descurtir(Long usuarioId, Long tipoConteudoId, Long conteudoId) {
        if (!curtidaRepository.existsByUsuarioIdAndTipoConteudoIdAndConteudoId(usuarioId, tipoConteudoId, conteudoId)) {
            throw new RuntimeException("Usuario nao curtiu este conteudo!");
        }

        try {
            curtidaRepository.deleteByUsuarioIdAndTipoConteudoIdAndConteudoId(usuarioId, tipoConteudoId, conteudoId);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Nao e possivel remover a curtida!");
        }
    }

    public boolean isCurtido(Long usuarioId, Long tipoConteudoId, Long conteudoId) {
        return curtidaRepository.existsByUsuarioIdAndTipoConteudoIdAndConteudoId(usuarioId, tipoConteudoId, conteudoId);
    }

    public long getCurtidasCount(Long tipoConteudoId, Long conteudoId) {
        return curtidaRepository.countByTipoConteudoIdAndConteudoId(tipoConteudoId, conteudoId);
    }

    private void validarConteudoExiste(TipoConteudoEnum tipoConteudo, Long conteudoId) {
        switch (tipoConteudo) {
            case POSTAGEM -> {
                if (!postagemRepository.existsById(conteudoId)) {
                    throw new RuntimeException("Postagem nao encontrada!");
                }
            }
            case COMENTARIO -> {
                if (!comentarioRepository.existsById(conteudoId)) {
                    throw new RuntimeException("Comentario nao encontrado!");
                }
            }
            case SUBCOMENTARIO -> {
                if (!subComentarioRepository.existsById(conteudoId)) {
                    throw new RuntimeException("Subcomentario nao encontrado!");
                }
            }
            case PROJETO -> {
                if (!projetoRepository.existsById(conteudoId)) {
                    throw new RuntimeException("Projeto nao encontrado!");
                }
            }
            default -> throw new RuntimeException("Tipo de conteudo invalido!");
        }
    }

}
