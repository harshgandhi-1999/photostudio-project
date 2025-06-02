package com.example.photostudio.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Component
public class JwtTokenProvider {
    private static final String AUTHORIZATION = "Authorization";

    private static final Logger logger = Logger.getLogger(JwtTokenProvider.class.getName());

    @Value("${jwt.JWT_SECRET}")
    private String JWT_SECRET;

    @Value("${jwt.EXPIRATION_TIME}")
    private long EXPIRATION_TIME;

    public String generateToken(String username, List<String> roles) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("roles", roles);
        return Jwts.builder()
//                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            // token expiration is also checked in this
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            logger.warning("JWT expired: " + e.getMessage());
        } catch (io.jsonwebtoken.UnsupportedJwtException e) {
            logger.warning("Unsupported JWT: " + e.getMessage());
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            logger.warning("Malformed JWT: " + e.getMessage());
        } catch (io.jsonwebtoken.SignatureException e) {
            logger.warning("Invalid JWT signature: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.warning("Empty or null JWT: " + e.getMessage());
        }
        return false;
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody().getSubject();
    }

    public List<String> getRoles(String token) {
        Claims claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
        return claims.get("roles", List.class);
    }

 /* private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
    }

    private List<SimpleGrantedAuthority> getRolesFromToken(String token) {
        List<SimpleGrantedAuthority> roles = null;
        Claims claims = extractAllClaims(token);
        Boolean isAdmin = claims.get("isAdmin", Boolean.class);
        Boolean isUser = claims.get("isUser", Boolean.class);
        if (isAdmin != null && isAdmin) {
            roles = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        if (isUser != null && isUser) {
            roles = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return roles;
    }*/
}