package com.challenge.operations.jwt;

import com.challenge.operations.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtRequestFilter is responsible for filtering HTTP requests to validate the presence
 * and validity of JWT tokens. It extends the OncePerRequestFilter to ensure that the
 * filter is executed once per request.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    /**
     * The CustomUserDetailsService instance responsible for
     * loading user-specific data during the JWT authentication process.
     *
     * This service is used to fetch user details required for
     * validating the JWT token and constructing the
     * UsernamePasswordAuthenticationToken.
     *
     * @see CustomUserDetailsService#loadUserByUsername(String)
     */
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * A utility class for handling JWT operations such as extracting usernames, validating tokens,
     * and checking token expiration. It is typically used in conjunction with security-related
     * operations in the application.
     */
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Filters each HTTP request, checks the "Authorization" header for a JWT token,
     * validates the token, and sets the authentication context if the token is valid.
     *
     * @param request the HTTP request object containing the client request
     * @param response the HTTP response object to send the response
     * @param chain the filter chain to pass the request and response to the next filter
     * @throws ServletException if an exception occurs that interferes with the filter's normal operation
     * @throws IOException if an I/O exception occurs during the filtering process
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        chain.doFilter(request, response);
    }
}
