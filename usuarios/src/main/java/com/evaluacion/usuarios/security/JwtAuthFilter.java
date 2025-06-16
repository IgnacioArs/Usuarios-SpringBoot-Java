package com.evaluacion.usuarios.security;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import com.evaluacion.usuarios.model.User;
import com.evaluacion.usuarios.repository.UsersRepository;
import org.springframework.stereotype.Component;
import javax.servlet.Filter;

@Component
public class JwtAuthFilter extends OncePerRequestFilter{
    
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsersRepository usersRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.replace("Bearer ", "");

            if (jwtUtil.validateToken(jwt)) {
                String email = jwtUtil.getEmailFromToken(jwt);
                Optional<User> userOpt = usersRepository.findByEmail(email);

                if (userOpt.isPresent()) {
                    // Si quieres puedes setear un usuario autenticado en el contexto de Spring Security
                    // SecurityContextHolder.getContext().setAuthentication(...);
                    request.setAttribute("user", userOpt.get()); // más simple
                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuario no encontrado");
                    return;
                }
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
