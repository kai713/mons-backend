package com.kairgaliyev.backendonlineshop.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotFoundException extends RuntimeException {
    private final Object[] params;
    private final String localizationKey;

    public NotFoundException(String message, String localizationKey) {
        this(message, localizationKey, new Object[]{});
    }

    public NotFoundException(String message, String localizationKey, Object... params) {
        super(message);
        this.params = params;
        this.localizationKey = localizationKey;
    }
}
