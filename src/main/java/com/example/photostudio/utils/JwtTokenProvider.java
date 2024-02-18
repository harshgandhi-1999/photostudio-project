package com.example.photostudio.utils;

import com.example.photostudio.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenProvider {
    private static final String AUTHORIZATION = "Authorization";

    @Autowired
    private UserService userService;

    @Value("${jwt.JWT_SECRET}")
    private String JWT_SECRET;

    @Value("${jwt.EXPIRATION_TIME}")
    private int EXPIRATION_TIME;

    // generate token for user
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }


    public String resolveToken(HttpServletRequest req) {
        //check authorization header
        String bearerToken = req.getHeader(AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public void validateToken(String token) throws JwtException, IllegalArgumentException {
        Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Authentication getAuthentication(String token) {
        //using database: uncomment when you want to fetch data from data base
        UserDetails userDetails = userService.loadUserByUsername(extractUsername(token));
        //UserProfileDto userProfileDto = userService.getUserByUsername(extractUsername(token));

        //from token take user value. comment below line for changing it taking from data base
        // org.springframework.security.core.userdetails.UserDetails userDetails = getUserDetails(token);
        return new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());

    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)).signWith(SignatureAlgorithm.HS256, JWT_SECRET).compact();
    }

    private String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
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

    /*    private List<SimpleGrantedAuthority> getRolesFromToken(String token) {
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