package com.ordernest.filter;

import com.ordernest.dto.JwtClaims;
import com.ordernest.security.CustomUserDetails;
import com.ordernest.security.CustomUserDetailsService;
import com.ordernest.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthFilter(JwtService jwtService, CustomUserDetailsService customUserDetailsService) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            String rawToken = token.substring(7);
            try {
                JwtClaims claim = jwtService.extractClaims(rawToken);
                UUID userId = claim.userId();
                String email = claim.email();

                if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(email);
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            customUserDetails, null, customUserDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (ExpiredJwtException e) {
                writeErrorResponse(response, "Token has expired");
                return;
            } catch (SignatureException e) {
                writeErrorResponse(response, "Invalid token signature");
                return;
            } catch (Exception e) {
                writeErrorResponse(response, "Invalid token");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void writeErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}