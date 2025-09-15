package io.github.jhairs2.todo_list.todo.service;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import io.github.jhairs2.todo_list.todo.repository.TodoUserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {

    @Value("${spring.security.oauth2.resourceserver.jwt.secret}")
    private String secretKey;

    private final String issuer = "self";
    private final TodoUserRepository todoUserRepository;

    public JWTService(TodoUserRepository todoUserRepository) {
        this.todoUserRepository = todoUserRepository;
    }

    public SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username, Collection<? extends GrantedAuthority> roles) {

        String userId = todoUserRepository.findByUsername(username).get().getId().toString();
        Map<String, Object> claims = new HashMap<>();

        claims.put("authorities", roles.stream().map(role -> role.getAuthority()).toList());
        claims.put("username", username);

        return Jwts.builder()
                .claims(claims)
                .subject(userId)
                .issuer(issuer)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000L))
                .signWith(getSigningKey())
                .compact();

    }
}
