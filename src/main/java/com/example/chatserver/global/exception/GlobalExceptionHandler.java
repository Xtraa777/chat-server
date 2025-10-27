package com.example.chatserver.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e,
        HttpServletRequest request) {

        ErrorCode errorCode = e.getErrorCode();

        log.warn("BusinessException: code={}, message={}, path={}",
            errorCode.name(), e.getMessage(), request.getRequestURI());

        ErrorResponse response = ErrorResponse.of(
            errorCode,
            request.getRequestURI()
        );

        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
        MethodArgumentNotValidException e, HttpServletRequest request) {

        String errorMessage = e.getBindingResult()
            .getAllErrors()
            .getFirst()
            .getDefaultMessage();

        log.warn("ValidationException: message={}, path={}",
            errorMessage, request.getRequestURI());

        ErrorResponse response = ErrorResponse.of(
            HttpStatus.BAD_REQUEST,
            errorMessage,
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
        Exception e, HttpServletRequest request) {

        log.error("Unexpected error: message={}, path={}",
            e.getMessage(), request.getRequestURI(), e);

        ErrorResponse response = ErrorResponse.of(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "예상치 못한 오류가 발생했습니다.",
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
