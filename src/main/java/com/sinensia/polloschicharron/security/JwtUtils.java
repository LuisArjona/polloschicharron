package com.sinensia.polloschicharron.security;

import java.security.Key;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${polloschicharron.app.jwt-secret}")
    private String jwtSecret;

    @Value("${polloschicharron.app.jwt-expiration-ms}")
    private int jwtExpirationMs;
    
    /**
     * Obtiene los detalles del usuario y construye el token
     * estableciendo el id de usuario como sujeto
     * y la fecha de creacion y expiracion (+1h) y lo firma
     * con nuestro secreto y el algoritmo elegido.
     * 
     * @param authentication
     * @return token generado
     */
    public String generarJwt(Authentication authentication) {

        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userDetailsImpl.getId().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(decodificarSecreto(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * Parsea los claims del token, obtiene el body y de ahi el subject
     * con el id del token
     * 
     * @param token
     * @return
     */
    public Long getUserIdFromToken(String token) {
    	
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(decodificarSecreto())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    /**
     * Parsea el token y verifica que su firma coincide con la de nuestra secret key.
     * 
     * @param token
     * @return validez
     */
    public boolean validarJwt(String token) {

        try {
            Jwts.parserBuilder().setSigningKey(decodificarSecreto()).build().parse(token);
            return true;
        } catch (MalformedJwtException e) {
        	System.out.println("a");
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
        	System.out.println("a");
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
        	System.out.println("a");
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
        	System.out.println("a");
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
   
    /**
     * Decodifica el secreto para validar tokens
     * 
     * @return secreto decodificado
     */
    private Key decodificarSecreto() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

}
