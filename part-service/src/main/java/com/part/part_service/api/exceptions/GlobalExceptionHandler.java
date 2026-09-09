package com.part.part_service.api.exceptions;

import com.part.part_service.domain.exceptions.DomainException;
import com.part.part_service.domain.exceptions.PartAlreadyExistsException;
import com.part.part_service.domain.exceptions.PartNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final MessageSource messageSource;

    private static final Map<Class<? extends DomainException>, HttpStatus> STATUS_MAP = Map.of(
        PartNotFoundException.class, HttpStatus.NOT_FOUND,
        PartAlreadyExistsException.class, HttpStatus.CONFLICT
    );

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handle(DomainException ex, Locale locale) {
        HttpStatus status = STATUS_MAP.getOrDefault(ex.getClass(), HttpStatus.INTERNAL_SERVER_ERROR);
        String message = messageSource.getMessage(ex.getMessageKey(), null, locale);

        return ResponseEntity
            .status(status)
            .body(new ErrorResponse(
                status.getReasonPhrase(),
                message,
                ex.getCode()
            ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining("; "));

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                message,
                "GEN00400"
            ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, Locale locale) {
        logger.error("GlobalExceptionHandler.handleGeneric - Unexpected error: {}", ex.getMessage(), ex);

        String message = messageSource.getMessage("error.internal", null, locale);
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                message,
                "GEN00500"
            ));
    }
}
