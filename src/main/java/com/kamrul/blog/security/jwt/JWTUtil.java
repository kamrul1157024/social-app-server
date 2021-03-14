package com.kamrul.blog.security.jwt;

import com.kamrul.blog.exception.UnauthorizedException;
import com.kamrul.blog.security.models.AppUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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


    public static Long getUserIdFromJwt(String jwtFromReq) throws UnauthorizedException {
        String jwt=jwtFromReq.substring(7);
        Long userId=new JWTUtil().extractUserId(jwt);
       return userId;
    }


    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver) throws UnauthorizedException {
        final Claims claims=extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims (String token) throws UnauthorizedException {
        try {
            return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        }
        catch (ExpiredJwtException jwtException)
        {
            throw new UnauthorizedException("UnValid JWT, LogIn again");
        }
    }

    public Long extractUserId(String token) throws UnauthorizedException {
        return Long.parseLong(extractClaim(token,Claims::getSubject));
    }
    public Date extractExpiration(String token) throws UnauthorizedException {
        return extractClaim(token,Claims::getExpiration);
    }


    private Boolean isTokenExpired(String token) throws UnauthorizedException {
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
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*24*7))
                .signWith(SignatureAlgorithm.HS256,SECRET_KEY)
                .compact();
    }

    public Boolean validateToken(String token, AppUserDetails userDetails) throws UnauthorizedException {
        final Long userId=extractUserId(token);
        return (userId.equals(userDetails.getUserId()) && !isTokenExpired(token));
    }
}
