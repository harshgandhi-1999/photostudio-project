package com.example.photostudio.utils;

import com.example.photostudio.dto.UserProfileDto;
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

    public boolean validateToken(HttpServletRequest request,UserDetails userDetails,String token) throws JwtException, IllegalArgumentException {
        Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
        String username = extractUsername(token);
        String usernameQueryParam = request.getParameter("username");

        if(username==null || usernameQueryParam==null || userDetails==null){
            return false;
        }

        return username.equals(usernameQueryParam) && username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Authentication getAuthentication(UserDetails userDetails) {
        // can use any method below:

        //UserDetails userDetails = userService.loadUserByUsername(extractUsername(token));
        //UserProfileDto userProfileDto = userService.getUserByUsername(extractUsername(token));


        // can also pass userDetails.getAuthorities() instead of null.
        return new UsernamePasswordAuthenticationToken(userDetails, null,null);

    }

    public UserDetails getUserDetails(String token){
        return userService.loadUserByUsername(extractUsername(token));
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