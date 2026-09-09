package com.client.client_service.api.exceptions;

public record ErrorResponse(
    String title,
    String message,
    String code
) {
}
