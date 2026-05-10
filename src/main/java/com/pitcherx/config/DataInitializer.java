package com.pitcherx.config;

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
	CommandLineRunner criarRolesInicial(RoleRepository roleRepository) {
		return args -> {
			if (roleRepository.count() == 0) {
				
				String[] roles = {"EMPRESA", "ADMIN", "USUARIO"};
				
				for(String roleS : roles) {
					Role role = new Role();
					role.setNomeRole(roleS);
					roleRepository.save(role);
				}
				IO.println("Roles adicionadas ao banco de dados.");
			}
		};
	}
	
	@Bean
	CommandLineRunner criarTiposVinculoInicial(TipoVinculoRepository tipoVinculoRepository) {
		return args -> {
			if (tipoVinculoRepository.count() == 0) {
				
				String[] tipoVinculos = {"CRIADOR", "SOCIO", "INVESTIDOR"};
				
				for(String tpVinculo : tipoVinculos) {
					TipoVinculo tipoVinculo = new TipoVinculo();
					tipoVinculo.setNomeTipoVinculo(tpVinculo);
					tipoVinculoRepository.save(tipoVinculo);
				}
				IO.println("Tipos de vinculo adicionadas ao banco de dados.");
			}
		};
	}
	
	@Bean
    CommandLineRunner criarAdminInicial(UsuarioRepository usuarioRepository, RoleRepository roleRepository) {
        return args -> {
            if (!usuarioRepository.existsUsuarioByEmailUsuario("adm@gmail.com")) {
                Usuario admin = new Usuario();
                admin.setNomeUsuario("admin");
                admin.setEmailUsuario("adm@gmail.com");
                admin.setSenhaUsuario("adm1234567");
                Role role = roleRepository.findRoleByNomeRole("ADMIN")
                		.orElseThrow(() -> new RuntimeException("Se role com o nome informado!"));
                admin.setRole(role);
                usuarioRepository.save(admin);
            }
            IO.println("Admin inicial adicionado ao banco de dados.");
        };
    }
	
}
