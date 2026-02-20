package com.salvatore.GestioneEventi.security;

import com.salvatore.GestioneEventi.entities.Utente;
import com.salvatore.GestioneEventi.exceptions.UnauthorizedException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTTools {

    @Value("${jwt.secret}")
    private String secret;

    //usiamo il metodo build per creare il token
    public String generateToken(Utente utente) {
        return Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))// RICORDA DI METTERLA SEMPRE IN MILLISECONDI
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                .setSubject(String.valueOf(utente.getId())) // stabilisco a chi appartiene il token (utente)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes())) // gli fornisco il secret che il server conosce
                .compact();
    }

    public Long verifyToken(String token) {

        try {
            Jwts.parserBuilder() // alternativa a parser
                    .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build()
                    .parseClaimsJws(token);
        } catch (Exception ex) {
            throw new UnauthorizedException("problemi col token!");
        }
        return null;
    }

    public Long extractIdFromToken(String token){
        return Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject());
    }
}
// serve per impostare e creare ed assegnare il token ad un'entità specifica, in questo caso il dipendente che potrà
// accedere e creare o impostare nuovi voli