package com.alcognerd.habittracker.filters;

import com.alcognerd.habittracker.dto.ApiResponse;
import com.alcognerd.habittracker.model.User;
import com.alcognerd.habittracker.repository.UserRepository;
import com.alcognerd.habittracker.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
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
        String path = request.getRequestURI();
        if ("/api/notification-data".equals(path) ||"/api/ping".equals(path)) {
            filterChain.doFilter(request, response);
            return;
        }


        if (cookies == null || cookies.length == 0) {
            writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Authentication cookie is missing");
            return;
        }

        Cookie tokenCookie = null;
        for (Cookie c : cookies) {
            if ("token".equals(c.getName())) {
                tokenCookie = c;
                break;
            }
        }

        if (tokenCookie == null) {
            writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token cookie not found");
            return;
        }

        String token = tokenCookie.getValue();
        if (token == null || token.isBlank()) {
            writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token cookie is empty");
            return;
        }

        try {
            String userId = jwtUtil.extractUserId(token);  // may throw if invalid/expired

            Long id = Long.valueOf(userId);
            User user = userRepo.findById(id).orElse(null);

            if (user == null) {
                writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "User not found for provided token");
                return;
            }

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(user, null, List.of());
            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (JwtException e) { // use your specific JWT exception type
            writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
            return;
        } catch (NumberFormatException e) {
            writeErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid user id in token");
            return;
        } catch (Exception e) {
            writeErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unexpected authentication error");
            return;
        }

        // everything ok -> continue
        filterChain.doFilter(request, response);
    }


    private void writeErrorResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");

        ApiResponse<Void> apiResponse = new ApiResponse<>("error", message);

        ObjectMapper mapper = new ObjectMapper();
        String body = mapper.writeValueAsString(apiResponse);

        response.getWriter().write(body);
    }

}

