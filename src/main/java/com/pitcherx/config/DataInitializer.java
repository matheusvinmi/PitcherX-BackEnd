package com.pitcherx.config;

import com.pitcherx.security.RoleType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pitcherx.model.Role;
import com.pitcherx.model.TipoVinculo;
import com.pitcherx.model.Usuario;
import com.pitcherx.repository.RoleRepository;
import com.pitcherx.repository.TipoVinculoRepository;
import com.pitcherx.repository.UsuarioRepository;

@Configuration
public class DataInitializer {

	@Bean
	CommandLineRunner inicializarDados(
			RoleRepository roleRepository,
			TipoVinculoRepository tipoVinculoRepository,
			UsuarioRepository usuarioRepository) {

		return args -> {
			if (roleRepository.count() == 0) {
				for (RoleType tipo : RoleType.values()) {
					roleRepository.save(new Role(tipo));
				}
				IO.println("Roles adicionadas ao banco de dados.");
			}

			if (tipoVinculoRepository.count() == 0) {
				String[] tipoVinculos = {"CRIADOR", "SOCIO", "INVESTIDOR"};
				for(String tpVinculo : tipoVinculos) {
					TipoVinculo tipoVinculo = new TipoVinculo();
					tipoVinculo.setNomeTipoVinculo(tpVinculo);
					tipoVinculoRepository.save(tipoVinculo);
				}
				IO.println("Tipos de vinculo adicionadas ao banco de dados.");
			}

			if (!usuarioRepository.existsUsuarioByEmailUsuario("adm@gmail.com")) {
				Usuario admin = new Usuario();
				admin.setNomeUsuario("admin");
				admin.setEmailUsuario("adm@gmail.com");
				admin.setSenhaUsuario("adm1234567");

				Role role = roleRepository.findRoleByNomeRole(RoleType.ADMIN)
						.orElseThrow(() -> new RuntimeException("Sem role com o nome informado!"));

				admin.setRole(role);
				usuarioRepository.save(admin);
				IO.println("Admin inicial adicionado ao banco de dados.");
			}
		};
	}
}