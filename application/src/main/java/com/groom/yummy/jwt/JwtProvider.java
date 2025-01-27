package com.groom.yummy.jwt;

import com.groom.yummy.exception.CustomException;
import com.groom.yummy.exception.JwtErrorCode;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
public class JwtProvider {
    private SecretKey secretKey;

    @Value("${spring.jwt.valid-time}")
    public Long VALID_TIME;

    @Value("${spring.jwt.cookie-name}")
    public String COOKIE_NAME;

    private JwtProvider(@Value("${spring.jwt.secret}") String secret){
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String createAccessToken(Long userId, String email, String nickname, String role){
        Date timeNow = new Date(System.currentTimeMillis());
        Date expirationTime = new Date(timeNow.getTime() + VALID_TIME);

        return Jwts.builder()
                .claim("userId", userId)
                .claim("email",email)
                .claim("nickname", nickname)
                .claim("role",role)
                .setIssuedAt(timeNow)
                .setExpiration(expirationTime)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Long getUserId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("userId", Long.class);
    }
    public String getUsername(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("email", String.class);
    }

    public String getName(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("nickname", String.class);
    }

    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public boolean validateToken(String token){
        //log.info("토큰 유효성 검증 시작");
        return valid(secretKey, token);
    }

    private boolean valid(SecretKey secretKey, String token){
        try{
            Jws<Claims> claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return claims.getBody().getExpiration().before(new Date());
        }catch (SignatureException ex){
            throw new CustomException(JwtErrorCode.WRONG_TYPE_TOKEN);
        }catch (MalformedJwtException ex){
            throw new CustomException(JwtErrorCode.UNSUPPORTED_TOKEN);
        }catch (ExpiredJwtException ex){
            throw new CustomException(JwtErrorCode.EXPIRED_TOKEN);
        }catch (IllegalArgumentException ex){
            throw new CustomException(JwtErrorCode.WRONG_TYPE_TOKEN);
        }
    }

}


