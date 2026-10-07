package com.pitcherx.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
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
@Table(name = "projeto")
public class Projeto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_projeto")
	private Long idProjeto;
	
	@Column(name = "nome_projeto", nullable = false)
	private String nomeProjeto;
	
	@Column(name = "descricao_projeto", nullable = false)
	private String descricaoProjeto;

	@Column(name = "meta_financeira", nullable = false)
	private Double metaFinanceira;

	@Column(name = "valor_arrecadado")
	private Double valorArrecadado;

	//Posteriormente criar uma classe somente para o risco de projeto
	@Column(name = "risco_projeto")
	private String riscoProjeto;

	@Column(name = "data_inicio_projeto", nullable = false)
	private	LocalDate dataInicioProjeto;
	
	@Column(name = "data_fim_projeto", nullable = false)
	private LocalDate dataFimProjeto;
	
	@ManyToOne
	@JoinColumn(name = "tipo_projeto_id", nullable = false)
	private TipoProjeto tipoProjeto;
	
	@Column(name = "is_active_projeto", nullable = false)
	private Boolean active = true;
	
	@Column(name = "url_imagem_projeto")
	private String urlImagemProjeto;

	@OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProjetoUsuario> usuarios = new ArrayList<>();

	@OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("ordem ASC")
	private List<ProjetoImagem> imagens = new ArrayList<>();

}
