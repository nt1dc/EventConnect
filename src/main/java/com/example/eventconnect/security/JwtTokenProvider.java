package com.example.eventconnect.security;


import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {


    private final UserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.header}")
    private String authorizationHeader;
    @Value("${jwt.access.expiration}")
    private long ACCESS_TOKEN_EXPIRATION;
    @Value("${jwt.refresh.expiration}")
    private long REFRESH_TOKEN_EXPIRATION;

    public JwtTokenProvider(UserDetailsService userDetailsService) {

        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    private void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }


    public String createAccessToken(String username, String role) {
        return createToken(username, role, ACCESS_TOKEN_EXPIRATION);
    }

    public String createRefreshToken(String username, String role) {
        return createToken(username, role, REFRESH_TOKEN_EXPIRATION);
    }

    private String createToken(String username, String role, long refreshTokenExpiration) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", role);
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenExpiration * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String createAccessTokenByRefreshToken(String token) {
        try {
            if (token != null && validateToken(token)) {
                Authentication authentication = getAuthentication(token);
                if (authentication != null) {
                    String accessToken = createAccessToken(getUsernameFromToken(token), getRoleFromToken(token));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    return accessToken;
                } else {
                    throw new JwtException("Cannot authenticate Refresh token");
                }
            } else {
                throw new JwtException("Cannot validate Refresh Token");
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            throw new JwtException("Cannot create Access Token by Refresh Token: ", e);
        }
    }

    public boolean validateToken(String token) throws JwtException {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        return !claimsJws.getBody().getExpiration().before(new Date());
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsernameFromToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String getRoleFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("role", String.class);
    }

    public Date getExpiryFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getExpiration();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(authorizationHeader);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }


}