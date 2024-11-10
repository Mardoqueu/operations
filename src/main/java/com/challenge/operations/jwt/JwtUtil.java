package com.challenge.operations.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

/**
 * JwtUtil is a utility class that provides methods for extracting information from
 * JSON Web Tokens (JWT), validating tokens, and checking token expiration. It leverages
 * a secret key for signing and verifying the JWT.
 */
@Component
public class JwtUtil {

    /**
     * This variable holds the secret key used for signing and verifying JWT tokens.
     * It is injected from the application properties using the @Value annotation.
     */
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    /**
     * Extracts the username from the given JWT token.
     *
     * @param token the JWT token from which to extract the username
     * @return the username extracted from the token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from the provided JWT token.
     *
     * @param token the JWT token from which to extract the expiration date
     * @return the expiration date of the token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a claim from the specified JWT token using the provided claims resolver function.
     *
     * @param <T> the type of the claim to be returned
     * @param token the JWT token from which the claim is to be extracted
     * @param claimsResolver a function to resolve a claim from the claims object
     * @return the claim extracted from the token as resolved by the claimsResolver
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the provided JWT token.
     *
     * @param token the JWT token from which to extract claims
     * @return the claims contained within the token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    /**
     * Checks if the provided JWT token has expired.
     *
     * @param token the JWT token to check
     * @return true if the token has expired, false otherwise
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Validates the provided JWT token by comparing the username extracted from the token
     * with the username from the provided UserDetails object and checking if the token has expired.
     *
     * @param token the JWT token to be validated
     * @param userDetails the UserDetails object to compare with the token's username
     * @return true if the token is valid, false otherwise
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
