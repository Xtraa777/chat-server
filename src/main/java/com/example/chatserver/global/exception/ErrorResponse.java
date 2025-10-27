package com.example.chatserver.global.exception;

import java.time.LocalDateTime;
import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
@Builder
public class ErrorResponse {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String code;
    private final String message;
    private final String path;

    public static ErrorResponse of(ErrorCode errorCode, String path) {
        return ErrorResponse.builder()
            .status(errorCode.getStatus().value())
            .error(errorCode.getStatus().getReasonPhrase())
            .code(errorCode.name())
            .message(errorCode.getMessage())
            .path(path)
            .build();
    }

    public static ErrorResponse of(HttpStatus status, String message, String path) {
        return ErrorResponse.builder()
            .status(status.value())
            .error(status.getReasonPhrase())
            .code("VALIDATION_ERROR")
            .message(message)
            .path(path)
            .build();
    }
}