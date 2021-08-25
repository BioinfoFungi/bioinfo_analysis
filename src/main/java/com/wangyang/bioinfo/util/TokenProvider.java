package com.wangyang.bioinfo.util;

import com.wangyang.bioinfo.pojo.authorize.Role;
import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.authorize.UserDetailDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wangyang
 * @date 2021/6/11
 */
@Component
@Slf4j
public class TokenProvider implements InitializingBean {

    private final String base64Secret;
    public final String AUTHORITIES_KEY="auth";
    public TokenProvider(@Value("${jwt.base64-secret}") String base64Secret) {
        this.base64Secret = base64Secret;
    }
    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(base64Secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    private Key key;



    public Token generateToken(UserDetailDTO user) {
        String authorities = user.getRoles().stream()
                .map(Role::getEnName)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + 24*60*60*1000);;


        String token = Jwts.builder()
                .setSubject(user.getUsername())
                .claim("ID", user.getId())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
        return new Token(token,validity.getTime());
    }
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(authToken);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: {}", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: {}", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: {}", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: {}", e);
        }
        return false;
    }
    public UserDetailDTO getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
        Set<Role> roles = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(name -> {
                    Role role = new Role();
                    return role;
                }).collect(Collectors.toSet());

        int id = (Integer)claims.get("ID");
        String subject = claims.getSubject();
        UserDetailDTO user=new UserDetailDTO();
        user.setId(id);
        user.setRoles(roles);
        user.setUsername(subject);
        return null;
    }
}
