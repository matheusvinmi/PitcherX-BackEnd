package com.pitcherx.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "status_projeto")
public class StatusProjeto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_status_projeto")
	private Long idStatusProjeto;
	
	@Column(name = "status_pj", nullable = false)
	private Status status;
	
	@Column(name = "data_status", nullable = false)
	private LocalDateTime dataStatus;
	
	@Column(name = "descricao_status")
	private String descricaoStatus;
	
	@ManyToOne
	@JoinColumn(name = "projeto_id", nullable = false)
	private Projeto projeto;

}
