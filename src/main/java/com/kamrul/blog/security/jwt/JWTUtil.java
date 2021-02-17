package com.kamrul.blog.security.jwt;

import com.kamrul.blog.security.models.AppUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTUtil {

    private final String SECRET_KEY="141D65%$14%2SA939H#20202f2425";


    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver)
    {
        final Claims claims=extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token)
    {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public Long extractUserId(String token)
    {
        return Long.parseLong(extractClaim(token,Claims::getSubject));
    }
    public Date extractExpiration(String token)
    {
        return extractClaim(token,Claims::getExpiration);
    }


    private Boolean isTokenExpired(String token)
    {
        return  extractExpiration(token).before(new Date());
    }

    public String generateToken(AppUserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        return createToken(claims,userDetails.getUserId().toString());
    }

    private String createToken(Map<String,Object> claims,String subject)
    {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*10))
                .signWith(SignatureAlgorithm.HS256,SECRET_KEY)
                .compact();
    }

    public Boolean validateToken(String token, AppUserDetails userDetails)
    {
        final Long userId=extractUserId(token);
        return (userId.equals(userDetails.getUserId()) && !isTokenExpired(token));
    }
}
