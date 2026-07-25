    package com.pitcherx.controller;

    import com.pitcherx.dto.usuario.*;

    import com.pitcherx.dto.usuario.esqueciSenha.EsqueciSenhaRequestDTO;
    import com.pitcherx.dto.usuario.esqueciSenha.ResetarSenhaRequestDTO;
    import com.pitcherx.dto.usuario.esqueciSenha.ValidarTokenRequestDTO;
    import com.pitcherx.dto.usuario.login.LoginRequestDTO;
    import com.pitcherx.dto.usuario.login.LoginResponseDTO;
    import com.pitcherx.service.SenhaResetService;
    import com.pitcherx.service.UsuarioService;
    import io.swagger.v3.oas.annotations.Operation;
    import io.swagger.v3.oas.annotations.tags.Tag;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.access.prepost.PreAuthorize;
    import org.springframework.validation.annotation.Validated;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping("/usuario")
    @Tag(name = "Usuario", description = "Endpoints relacionados a usuários")
    public class UsuarioController {

        private final UsuarioService usuarioService;
        private final SenhaResetService senhaResetService;

        public UsuarioController(UsuarioService usuarioService, SenhaResetService senhaResetService) {
            this.usuarioService = usuarioService;
            this.senhaResetService = senhaResetService;
        }

        @GetMapping
        @PreAuthorize("hasRole('ADMIN')")
        @Operation(description = "Este endpoint faz a listagem de todos os usuários.")
        public ResponseEntity<List<UsuarioResponseDTO>> getUsuario() {
            return ResponseEntity.status(HttpStatus.OK).body(usuarioService.listarUsuarios());
        }

        @GetMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        @Operation(description = "Este endpoint faz a busca de usuário através do ID.")
        public ResponseEntity<UsuarioResponseDTO> getUsuarioById(Long id) {
            return ResponseEntity.status(HttpStatus.OK).body(usuarioService.buscarUsuarioPorId(id));
        }

        @PostMapping("/cadastro-usuario")
        @Operation(description = "Este endpoint faz o cadastro de usuário.")
        public ResponseEntity<UsuarioResponseDTO> saveUsuario(@Validated @RequestBody UsuarioRequestDTO usuarioRequestDTO) {
            UsuarioResponseDTO usuarioResponseDTO = usuarioService.criarUsuario(usuarioRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioResponseDTO);
        }

        @PutMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.idUsuario")
        @Operation(description = "Este endpoint faz a atualização de usuário através do ID.")
        public ResponseEntity<UsuarioResponseDTO> updateUsuario(@PathVariable Long id, @Validated @RequestBody UsuarioRequestDTO usuarioRequestDTO) {
            UsuarioResponseDTO usuarioResponseDTO = usuarioService.atualizarUsuario(id, usuarioRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).body(usuarioResponseDTO);
        }

        @PutMapping("/ativar-desativar/{id}")
        @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.idUsuario")
        @Operation(description = "Este endpoint faz a ativação ou desativação de usuário através do ID.")
        public ResponseEntity<UsuarioResponseDTO> ativarDesativarUsuario(@PathVariable Long id) {
            return ResponseEntity.status(HttpStatus.OK).body(usuarioService.ativarDesativarUsuario(id));
        }

        @DeleteMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.idUsuario")
        @Operation(description = "Este endpoint faz a exclusão de usuário através do ID.")
        public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
            usuarioService.deletarUsuario(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        @PostMapping("/login")
        @Operation(description = "Este endpoint faz o login de usuário.")
        public ResponseEntity<LoginResponseDTO> login(@Validated @RequestBody LoginRequestDTO loginRequestDTO) {
            LoginResponseDTO loginResponseDTO = usuarioService.login(loginRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).body(loginResponseDTO);
        }

        @PostMapping("/redefinir-senha/{id}")
        @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.idUsuario")
        @Operation(description = "Este endpoint faz a redefinição de senha de usuário.")
        public ResponseEntity<UsuarioResponseDTO> redefinirSenha(@PathVariable Long id, @Validated @RequestBody RedefinirSenhaRequestDTO redefinirSenhaRequestDTO) {
            UsuarioResponseDTO usuarioResponseDTO = usuarioService.redefinirSenha(id, redefinirSenhaRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).body(usuarioResponseDTO);
        }

        @PostMapping("/esqueci-senha")
        @Operation(description = "Este endpoint permite que o usuário solicite a redefinição de senha.")
        public ResponseEntity<Void> esqueciSenha(@Validated @RequestBody EsqueciSenhaRequestDTO esqueciSenhaRequestDTO) {
            senhaResetService.solicitarRedefinicaoSenha(esqueciSenhaRequestDTO.usuarioEmail());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        @PostMapping("/validar-token")
        @Operation(description = "Este endpoint permite que o usuário valide o token de redefinição de senha.")
        public ResponseEntity<Void> validarToken(@Validated @RequestBody ValidarTokenRequestDTO validarTokenRequestDTO) {
            senhaResetService.validarToken(validarTokenRequestDTO.emailUsuario(), validarTokenRequestDTO.codigoRedefinicao());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        @PostMapping("/resetar-senha")
        @Operation(description = "Este endpoint permite que o usuário redefina a senha.")
        public ResponseEntity<Void> resetarSenha(@Validated @RequestBody ResetarSenhaRequestDTO resetarSenhaRequestDTO) {
            senhaResetService.resetarSenha(resetarSenhaRequestDTO.emailUsuario(), resetarSenhaRequestDTO.codigoRedefinicao(), resetarSenhaRequestDTO.novaSenha());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

    }
