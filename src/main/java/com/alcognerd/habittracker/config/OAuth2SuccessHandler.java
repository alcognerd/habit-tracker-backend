package com.alcognerd.habittracker.config;

import com.alcognerd.habittracker.dto.ApiResponse;
import com.alcognerd.habittracker.model.User;
import com.alcognerd.habittracker.repository.UserRepository;
import com.alcognerd.habittracker.utils.CookieUtil;
import com.alcognerd.habittracker.utils.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepo;
    private final JwtUtil jwtUtil;
    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oAuthUser = (OAuth2User) authentication.getPrincipal();

        String googleId = oAuthUser.getAttribute("sub");
        String email = oAuthUser.getAttribute("email");
        String name = oAuthUser.getAttribute("name");
        String picture = oAuthUser.getAttribute("picture");

        // 1. Create or get user
        User user = userRepo.findByGoogleId(googleId)
                .orElseGet(() -> userRepo.save(
                        User.builder()
                                .googleId(googleId)
                                .email(email)
                                .name(name)
                                .picture(picture)
                                .build()
                ));

        // 2. Generate JWT
        String jwt = jwtUtil.generateToken(user.getId().toString());

        // 3. Set HttpOnly Cookie// 7 days

        response.addCookie(CookieUtil.createJwtCookie(jwt));
        response.sendRedirect(frontendUrl);

    }
}

