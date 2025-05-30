package com.kairgaliyev.backendonlineshop.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadRequestException extends RuntimeException {
    private final String localizationKey;
    private final Object[] params;

    public BadRequestException(String message, String localizationKey, Object... params) {
        super(message);
        this.localizationKey = localizationKey;
        this.params = params;
    }

    public BadRequestException(String message, String localizationKey) {
        this(message, localizationKey, new Object[]{});
    }
}
