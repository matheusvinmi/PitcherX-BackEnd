package com.pitcherx.service;

import com.pitcherx.dto.usuario.UsuarioRequestDTO;
import com.pitcherx.dto.usuario.UsuarioResponseDTO;
import com.pitcherx.mapper.UsuarioMapper;
import com.pitcherx.model.Usuario;
import com.pitcherx.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
     }

     @Transactional(readOnly = true)
     public List<UsuarioResponseDTO> listarUsuarios(){
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toDTO)
                .toList();
     }

     @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarUsuarioPorId(Long idUsuario){
         Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new EntityNotFoundException("Sem usuário com o ID informado!"));
         return usuarioMapper.toDTO(usuario);
     }

     @Transactional
    public UsuarioResponseDTO criarUsuario(UsuarioRequestDTO usuarioRequestDTO){

        if (usuarioRepository.existsUsuarioByEmailUsuario(usuarioRequestDTO.emailUsuario())){
            throw new IllegalArgumentException("Já existe um usuário com esse email!");
        }

        Usuario usuario = usuarioMapper.toEntity(usuarioRequestDTO);

        Usuario salvo = usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(salvo);
     }

     @Transactional
    public UsuarioResponseDTO atualizarUsuario(Long idUsuario, UsuarioRequestDTO usuarioRequestDTO){
         Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new EntityNotFoundException("Sem usuário com o ID informado!"));

         if (usuarioRepository.existsUsuarioByEmailUsuarioAndIdUsuarioNot(usuarioRequestDTO.emailUsuario(), idUsuario)){
             throw new IllegalArgumentException("Já existe um usuário com esse email!");
         }

         usuarioMapper.updateFromDTO(usuarioRequestDTO, usuario);

         Usuario salvo = usuarioRepository.save(usuario);
         return usuarioMapper.toDTO(salvo);
     }

     @Transactional
     public UsuarioResponseDTO ativarDesativarUsuario(Long idUsuario) {
         Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new EntityNotFoundException("Sem usuário com o ID informado!"));

         if (usuario.getActive()) {
             usuario.setActive(false);
         } else {
             usuario.setActive(true);
         }

            Usuario salvo = usuarioRepository.save(usuario);
            return usuarioMapper.toDTO(salvo);
     }

     @Transactional
     public void deletarUsuario(Long idUsuario){
        if (!usuarioRepository.existsById(idUsuario)){
            throw new EntityNotFoundException("Sem usuário com o ID informado!");
        }
        try {
            usuarioRepository.deleteById(idUsuario);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Não é possível deletar o usuário, pois ele está associada a outras entidades.!");
        }

     }

}
