package com.project.shopapp.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;


@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.secret_key}")
    private String secretKey;


    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date currenDate = new Date();
        Date expirationTime = new Date(currenDate.getTime() + expiration);
        String token = Jwts.builder().setSubject(username)
                .setExpiration(expirationTime)
                .setIssuedAt(new Date())
                .signWith(key())
                .compact();
        return token;
    }
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }


    public String getPhoneNumber(String token){
        return Jwts.parser().setSigningKey(key())
                .build().parseClaimsJws(token)
                .getBody().getSubject();
    }


    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(key())
                    .build().parse(token);
            return true;
        }catch (MalformedJwtException ex){
            throw new JwtException("Invalid JWT token");
        }catch (ExpiredJwtException ex){
            throw new JwtException("Expired JWT token");
        }catch (UnsupportedJwtException ex){
            throw new JwtException("Unsupported JWT token");
        }catch (IllegalArgumentException ex){
            throw new JwtException("JWT claims string is empty");
        }

    }




}
