package com.kairgaliyev.backendonlineshop.resolver;

import com.kairgaliyev.backendonlineshop.entity.UserEntity;
import com.kairgaliyev.backendonlineshop.exception.BadRequestException;
import com.kairgaliyev.backendonlineshop.security.CustomUserDetails;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * HandlerMethodArgumentResolver - при нахождении аннотации и параметра типа UserEntity в методе,
 * задействует метод resolveArgument
 */
@Component
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {
    /**
     * Проверяем поддерживает ли наш resolver параметры в методе
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class)
                && parameter.getParameterType().equals(UserEntity.class);
    }

    /**
     * Из SecurityContextHolder берем SecurityContext который мы вложили с помощью JWTAuthFilter
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userDetails.getUser();
        }
        throw new BadRequestException("Unauthorized", "error.bad_request");
    }
}
