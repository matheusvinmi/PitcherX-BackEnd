package com.pitcherx.service;

import com.pitcherx.dto.usuario.RedefinirSenhaRequestDTO;
import com.pitcherx.dto.usuario.UsuarioRequestDTO;
import com.pitcherx.dto.usuario.UsuarioResponseDTO;
import com.pitcherx.dto.usuario.login.LoginRequestDTO;
import com.pitcherx.dto.usuario.login.LoginResponseDTO;
import com.pitcherx.service.EmailService;
import com.pitcherx.mapper.UsuarioMapper;
import com.pitcherx.model.Role;
import com.pitcherx.model.Usuario;
import com.pitcherx.repository.RoleRepository;
import com.pitcherx.repository.UsuarioRepository;
import com.pitcherx.security.RoleType;
import com.pitcherx.security.TokenConfig;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenConfig tokenConfig;
    private final EmailService emailService;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper,
    		RoleRepository roleRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenConfig tokenConfig, EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenConfig = tokenConfig;
        this.emailService = emailService;
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
        
        Role role = roleRepository.findRoleByNomeRole(RoleType.USUARIO)
        		.orElseThrow(() -> new RuntimeException("Se role com o nome informado!"));

        Usuario usuario = usuarioMapper.toEntity(usuarioRequestDTO);
        String senhaHash = passwordEncoder.encode(usuarioRequestDTO.senhaUsuario());
        usuario.setSenhaUsuario(senhaHash);
        usuario.setRoles(Set.of(role));

        Usuario salvo = usuarioRepository.save(usuario);
        emailService.enviarEmailBoasVindas(usuarioRequestDTO.emailUsuario(), usuarioRequestDTO.nomeUsuario());
        return usuarioMapper.toDTO(salvo);
     }

     @Transactional
    public UsuarioResponseDTO atualizarUsuario(Long idUsuario, UsuarioRequestDTO usuarioRequestDTO){
         Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new EntityNotFoundException("Sem usuário com o ID informado!"));

         if (usuarioRepository.existsUsuarioByEmailUsuarioAndIdUsuarioNot(usuarioRequestDTO.emailUsuario(), idUsuario)){
             throw new IllegalArgumentException("Já existe um usuário com esse email!");
         }

         String senhaHash = passwordEncoder.encode(usuarioRequestDTO.senhaUsuario());
         usuario.setSenhaUsuario(senhaHash);
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
        emailService.enviarEmailBoasVindas(salvo.getEmailUsuario(), salvo.getNomeUsuario());
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

     @Transactional
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO){
        Usuario usuario = usuarioRepository.findUserByEmailUsuario(loginRequestDTO.emailUsuario())
                .orElseThrow(() -> new EntityNotFoundException("Sem usuário com o email informado!"));

         if (!passwordEncoder.matches(loginRequestDTO.senhaUsuario(), usuario.getSenhaUsuario())) {
             throw new IllegalArgumentException("Credenciais incorretas!");
         }
         UsernamePasswordAuthenticationToken authenticationToken =
                 new UsernamePasswordAuthenticationToken(
                         usuario,
                         null,
                         usuario.getAuthorities()
                 );

         SecurityContextHolder.getContext().setAuthentication(authenticationToken);

         String token = tokenConfig.generateToken(usuario);

         return new LoginResponseDTO(
                 usuario.getIdUsuario(),
                 usuario.getNomeUsuario(),
                 usuario.getEmailUsuario(),
                 usuario.getActive(),
                 token,
                 usuario.getRoles()
         );
     }

     @Transactional
    public UsuarioResponseDTO redefinirSenha(Long idUsuario, RedefinirSenhaRequestDTO redefinirSenhaRequestDTO) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new EntityNotFoundException("Sem usuário com o ID informado!"));

        if (!passwordEncoder.matches(redefinirSenhaRequestDTO.senhaAtual(), usuario.getSenhaUsuario())) {
            throw new IllegalArgumentException("A senha atual está incorreta!");
        }

        String novaSenhaHash = passwordEncoder.encode(redefinirSenhaRequestDTO.novaSenha());
        usuario.setSenhaUsuario(novaSenhaHash);

        Usuario salvo = usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(salvo);
     }

     @Transactional
     public UsuarioResponseDTO alterarRole(Long idUsuario, Long idRole){

         Usuario usuario = usuarioRepository.findById(idUsuario).
                 orElseThrow(() -> new EntityNotFoundException("Sem usuário com o ID informado!"));

         Role role = roleRepository.findById(idRole).
                 orElseThrow(() -> new EntityNotFoundException("Sem função com o ID informado!"));

         usuario.getRoles().add(role);

         Usuario salvo = usuarioRepository.save(usuario);
         return usuarioMapper.toDTO(salvo);
     }

}
