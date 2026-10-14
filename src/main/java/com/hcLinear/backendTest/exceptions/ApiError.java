package com.hcLinear.backendTest.exceptions;

import java.time.OffsetDateTime;
import java.util.List;

public record ApiError(
        String type,
        String message,
        List<String> details,
        int status,
        String path,
        OffsetDateTime timestamp
) {
    public ApiError(String type, String message, List<String> details, int status, String path) {
        this(type, message, details, status, path, OffsetDateTime.now());
    }
}
