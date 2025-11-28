package com.alcognerd.habittracker.utils;

import jakarta.servlet.http.Cookie;

public class CookieUtil {

    public static Cookie createJwtCookie(String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);          // JavaScript cannot read
        cookie.setSecure(true);            // HTTPS only
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);         // 1 hour
        cookie.setAttribute("SameSite", "None");  // Required for cross-site cookies
        return cookie;
    }

    public static Cookie clearJwtCookie() {
        Cookie cookie = new Cookie("token", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setAttribute("SameSite", "None");
        return cookie;
    }
}
