package com.kairgaliyev.backendonlineshop.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Locale;

@Component
@Slf4j
public class LocaleInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String lang = request.getHeader("Accept-Language");

        if (lang == null) {
            lang = request.getParameter("lang");
        }

        if (lang == null) {
            // Читаем язык из куки
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("lang".equals(cookie.getName())) {
                        lang = cookie.getValue();
                        break;
                    }
                }
            }
        }

        if (lang == null) {
            lang = "en";
        }
        Locale locale = Locale.forLanguageTag(lang);
        LocaleContextHolder.setLocale(locale);

        log.debug("Установлен язык: {}", locale);
        return true;
    }
}

