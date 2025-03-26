package com.kairgaliyev.backendonlineshop.dto;

import java.time.LocalDateTime;

public record ErrorResponse(Integer value, String description, LocalDateTime timestamp) {
}
