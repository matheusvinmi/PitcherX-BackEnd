package com.pitcherx.model;

import java.time.LocalDate;
import java.util.Date;

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
@Table(name = "postagem")
public class Postagem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_postegem")
	private Long idPostagem;
	
	@Column(name = "titulo_postagem", nullable = false)
	private String tituloPostagem;
	
	@Column(name = "texto_postagem", nullable = false)
	private String textoPostagem;
	
	@Column(name = "data_postagem", nullable = false)
	private LocalDate dataPostagem;
	
	@Column(name = "like_postagem", nullable = true)
	private Integer likePostagem;
	
	@Column(name = "dislike_postagem", nullable = true)
	private Integer dislikePostagem;
	
	@Column(name = "url_imagem_postagem", nullable = true)
	private String urlImagemPostagem;
	
	@ManyToOne
	@JoinColumn(name = "usuario_id", nullable = false)
	private Usuario usuario;
}
