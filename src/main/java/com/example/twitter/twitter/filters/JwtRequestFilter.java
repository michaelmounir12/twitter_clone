package com.example.twitter.twitter.filters;

import java.io.IOException;

import io.jsonwebtoken.JwtException;
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

import com.example.twitter.twitter.service.CustomUserDetailsService;
import com.example.twitter.twitter.util.JwtUtil;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws IOException, ServletException {

        final String authorizationHeader = request.getHeader("Authorization");
        String jwt = null;
        String username = null;

        // Check if the Authorization header is present and contains a Bearer token
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = jwUtil.extractUsername(jwt);  // Extract username from the token
            } catch (JwtException e) {
                logger.error("Invalid token: " + jwt, e);  // Log invalid token
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Optionally return unauthorized status
                return;  // Skip further processing if the token is invalid
            }
        }

        // If username is found and no authentication exists, authenticate the user
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Validate the token and ensure it's not expired
            if (jwUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken userPwdAutToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // Set the details (e.g., IP, browser info, etc.)
                userPwdAutToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authentication context
                SecurityContextHolder.getContext().setAuthentication(userPwdAutToken);
            } else {
                logger.warn("Token is expired or invalid: " + jwt);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // Optionally return unauthorized status
                return;  // Skip further processing if the token is invalid or expired
            }
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }
}
