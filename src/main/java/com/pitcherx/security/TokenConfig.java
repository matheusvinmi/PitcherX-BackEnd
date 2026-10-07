package com.pitcherx.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pitcherx.model.Usuario;

@Component
public class TokenConfig {

    private static final String SECRET_KEY = System.getProperty("jwt.secret.key", "secretKey");

    private final Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

    public String generateToken(Usuario usuario) {
        return JWT.create()
                .withClaim("idUsuario", usuario.getIdUsuario())
                .withSubject(usuario.getEmailUsuario())
                .withClaim("roles", usuario.getRoles().stream().map(role -> role.getNomeRole().name()).toList())
                .withExpiresAt(Instant.now().plus(365, ChronoUnit.DAYS))
                .withIssuedAt(Instant.now())
                .sign(algorithm);
    }

    public Optional<JWTUserData> validateToken(String token) {
        try {
            DecodedJWT decodedJWT = JWT.require(algorithm).build().verify(token);
            var idUsuario = decodedJWT.getClaim("idUsuario").asLong();
            var emailUsuario = decodedJWT.getSubject();
            var roles = decodedJWT.getClaim("roles").asList(String.class);
            return Optional.of(new JWTUserData(idUsuario, emailUsuario, roles.stream().map(roleName -> new com.pitcherx.model.Role(RoleType.valueOf(roleName))).collect(java.util.stream.Collectors.toSet())));
        } catch (JWTVerificationException e) {
            return Optional.empty();
        }
    }
}
