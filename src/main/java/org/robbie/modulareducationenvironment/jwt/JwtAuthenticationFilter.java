package org.robbie.modulareducationenvironment.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    //We have to pass the secret key since we cant inject it for filters
    private final String SECRET;
    public JwtAuthenticationFilter(String SECRET) {
        this.SECRET = SECRET;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String token = extractToken(request);

        if (token != null) {
            try {
                Algorithm algorithm = Algorithm.HMAC256(SECRET);
                DecodedJWT jwt = JWT.require(algorithm).build().verify(token);

                String userUUID = jwt.getClaim("uuid").asString();
                // Extract roles as a list
                List<String> roles = jwt.getClaim("role").asList(String.class);

                // Convert roles to SimpleGrantedAuthority list
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(role -> new SimpleGrantedAuthority(role.startsWith("ROLE_") ? role : "ROLE_" + role))
                        .collect(Collectors.toList());

                // Set authentication with multiple roles
                PreAuthenticatedAuthenticationToken auth =
                        new PreAuthenticatedAuthenticationToken(UUID.fromString(userUUID), null, authorities);

                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);

//                System.out.println("Authenticated user: " + userUUID + " with roles: " + roles);

            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT");
                return;
            }
        }
        chain.doFilter(request, response);
        this.resetAuthenticationAfterRequest();
//        org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println("Security context authentication: " + authentication);
    }

    private void resetAuthenticationAfterRequest() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    private String extractToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

    /**
     * Gets the users UUID. If the user is not authenticated RuntimeException is thrown.
     * @return the users UUID
     * @throws RuntimeException
     */
    public static UUID getUserUUID() throws RuntimeException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the user is authenticated
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new RuntimeException("User is not authenticated");
        }

        // The UUID was set as the principal in JwtAuthenticationFilter
        return (UUID) authentication.getPrincipal();
    }
}
