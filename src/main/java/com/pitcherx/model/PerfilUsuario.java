package com.pitcherx.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "perfil_usuario")
public class PerfilUsuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "perfil_usuario")
	private Long idPerfilUsuario;
	
	@Column(name = "linkedin", nullable = false)
	private String linkedin;
	
	@OneToMany
	@JoinColumn(name = "especialidade_id", nullable = false)
	private Especialidade especialidade;
	
}
