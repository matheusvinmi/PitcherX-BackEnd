package com.pitcherx.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "nome_usuario", nullable = false)
    private String nomeUsuario;

    @Column(name = "email_usuario", nullable = false, unique = true)
    private String emailUsuario;

    @Column(name = "senha_usuario", nullable = false)
    private String senhaUsuario;

    @Column(name = "telefone_usuario")
    private String telefoneUsuario;

    @Column(name = "url_imagem_usuario")
    private String urlImagemUsuario;

    @Column(name = "is_active_usuario", nullable = false)
    private Boolean active = true;
    
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "data_criacao_usuario", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime dataCriacaoUsuario;

    @Column(name = "data_atualizacao_usuario", nullable = false)
    @UpdateTimestamp
    private LocalDateTime dataAtualizacaoUsuario;

}
