package UmbertoAmoroso.u5s7d1.security;

import UmbertoAmoroso.u5s7d1.exceptions.UnauthorizedException;
import UmbertoAmoroso.u5s7d1.services.DipendenteService;
import UmbertoAmoroso.u5s7d1.entities.Dipendente;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JWTCheckFilter extends OncePerRequestFilter {

    private final JWTTools jwtTools;
    private final DipendenteService dipendenteService;

    @Autowired
    public JWTCheckFilter(JWTTools jwtTools, DipendenteService dipendenteService) {
        this.jwtTools = jwtTools;
        this.dipendenteService = dipendenteService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Per favore inserisci correttamente il token nell'Authorization Header");
        }

        String accessToken = authHeader.substring(7);

        jwtTools.verifyToken(accessToken);
        String id = jwtTools.extractIdFromToken(accessToken);

        Dipendente currentUser = dipendenteService.trovaPerId(Long.parseLong(id));

        Authentication authentication = new UsernamePasswordAuthenticationToken(currentUser, null, currentUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}
