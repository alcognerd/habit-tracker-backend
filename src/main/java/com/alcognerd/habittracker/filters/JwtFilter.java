package com.alcognerd.habittracker.filters;

import com.alcognerd.habittracker.model.User;
import com.alcognerd.habittracker.repository.UserRepository;
import com.alcognerd.habittracker.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("token")) {
                    try {
                        String userId = jwtUtil.extractUserId(c.getValue());

                        User user = userRepo.findById(Long.valueOf(userId)).orElse(null);

                        if (user != null) {
                            UsernamePasswordAuthenticationToken auth =
                                    new UsernamePasswordAuthenticationToken(user, null, List.of());
                            SecurityContextHolder.getContext().setAuthentication(auth);
                        }

                    } catch (Exception ignored) {}
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}

