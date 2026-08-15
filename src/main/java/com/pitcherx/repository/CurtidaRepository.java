package com.pitcherx.repository;

import com.pitcherx.model.Curtida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurtidaRepository extends JpaRepository<Curtida, Long> {

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Curtida c WHERE c.usuario.idUsuario = :usuarioId AND c.tipoConteudo.idTipoConteudo = :tipoConteudoId AND c.conteudoId = :conteudoId")
    boolean existsByUsuarioIdAndTipoConteudoIdAndConteudoId(@Param("usuarioId") Long usuarioId, @Param("tipoConteudoId") Long tipoConteudoId, @Param("conteudoId") Long conteudoId);

    @Query("SELECT COUNT(c) FROM Curtida c WHERE c.tipoConteudo.idTipoConteudo = :tipoConteudoId AND c.conteudoId = :conteudoId")
    long countByTipoConteudoIdAndConteudoId(@Param("tipoConteudoId") Long tipoConteudoId, @Param("conteudoId") Long conteudoId);

    @Query("SELECT c FROM Curtida c WHERE c.usuario.idUsuario = :usuarioId AND c.tipoConteudo.idTipoConteudo = :tipoConteudoId AND c.conteudoId = :conteudoId")
    Optional<Curtida> findByUsuarioIdAndTipoConteudoIdAndConteudoId(@Param("usuarioId") Long usuarioId, @Param("tipoConteudoId") Long tipoConteudoId, @Param("conteudoId") Long conteudoId);

    @Query("DELETE FROM Curtida c WHERE c.usuario.idUsuario = :usuarioId AND c.tipoConteudo.idTipoConteudo = :tipoConteudoId AND c.conteudoId = :conteudoId")
    void deleteByUsuarioIdAndTipoConteudoIdAndConteudoId(@Param("usuarioId") Long usuarioId, @Param("tipoConteudoId") Long tipoConteudoId, @Param("conteudoId") Long conteudoId);

}
