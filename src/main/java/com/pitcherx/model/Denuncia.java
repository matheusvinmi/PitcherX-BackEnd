package com.pitcherx.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "denuncia")
public class Denuncia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_denuncia")
    private Long idDenuncia;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_denuncia", nullable = false)
    private TipoDenuncia tipoDenuncia;

    @Column(name = "alvo_id", nullable = false)
    private Long alvoId;

    @Column(name = "motivo", nullable = false, length = 500)
    private String motivo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusDenuncia status = StatusDenuncia.PENDENTE;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime dataCriacao;

    @Column(name = "data_decisao")
    private LocalDateTime dataDecisao;

    @Column(name = "decisao_admin", length = 1000)
    private String decisaoAdmin;

    public enum TipoDenuncia {
        PERFIL, POSTAGEM, PROJETO, COMENTARIO
    }

    public enum StatusDenuncia {
        PENDENTE, ACEITA, REJEITADA
    }
}