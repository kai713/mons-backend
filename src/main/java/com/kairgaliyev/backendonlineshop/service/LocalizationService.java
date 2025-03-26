package com.kairgaliyev.backendonlineshop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocalizationService {
    private final MessageSource messageSource;

    /**
     * Метод принимает параметры и проверять их на корректность
     *
     * @param key  ключ
     * @return возвращает строку
     */
    public String getMessage(String key) {
        log.debug("Getting message {}", key);
        String message = messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
        log.debug("Message {}", message);
        return message;
    }

}
