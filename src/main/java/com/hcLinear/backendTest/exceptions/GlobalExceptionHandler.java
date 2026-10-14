package com.hcLinear.backendTest.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ApiError> build(String type, String message, List<String> details,
                                          HttpStatus status, HttpServletRequest request) {
        return ResponseEntity.status(status)
                .body(new ApiError(type, message, details, status.value(), request.getRequestURI()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return build("NOT_FOUND", ex.getMessage(), List.of(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({DuplicateResourceException.class, DataIntegrityViolationException.class})
    public ResponseEntity<ApiError> handleConflict(RuntimeException ex, HttpServletRequest request) {
        String message = ex instanceof DuplicateResourceException
                ? ex.getMessage()
                : "Adatintegritási hiba (pl. duplikált egyedi mező)";
        List<String> details = ex instanceof DataIntegrityViolationException
                ? List.of(rootCauseMessage(ex))
                : List.of();
        return build("CONFLICT", message, details, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<ApiError> handleBusiness(BusinessValidationException ex, HttpServletRequest request) {
        return build("VALIDATION_ERROR", ex.getMessage(), List.of(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleBeanValidation(MethodArgumentNotValidException ex,
                                                        HttpServletRequest request) {
        List<String> details = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .sorted()
                .toList();
        return build("VALIDATION_ERROR", "Validációs hiba", details, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex,
                                                             HttpServletRequest request) {
        List<String> details = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .sorted()
                .toList();
        return build("VALIDATION_ERROR", "Validációs hiba", details, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class,
            IllegalArgumentException.class})
    public ResponseEntity<ApiError> handleBadRequest(Exception ex, HttpServletRequest request) {
        return build("BAD_REQUEST", "Hibás kérés: " + ex.getMessage(), List.of(),
                HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiError> handleNoResource(NoResourceFoundException ex, HttpServletRequest request) {
        return build("NOT_FOUND", "Az erőforrás nem található: " + ex.getResourcePath(), List.of(),
                HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest request) {
        return build("INTERNAL_ERROR", "Váratlan szerverhiba", List.of(ex.getMessage() == null ? "" : ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private String rootCauseMessage(Throwable ex) {
        Throwable cause = ex;
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        return cause.getMessage() == null ? ex.getMessage() : cause.getMessage();
    }
}
