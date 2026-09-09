package com.part.part_service.api.exceptions;

public record ErrorResponse(
    String title,
    String message,
    String code
) {
}
