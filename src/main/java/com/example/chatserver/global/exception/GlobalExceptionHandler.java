package com.example.chatserver.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFoundException(NotFoundException e,
        HttpServletRequest request) {
        log.warn("Not Found Exception: message={}, path={}",
            e.getMessage(), request.getRequestURI());

        Map<String, String> response = new HashMap<>();
        response.put("message", "요청한 리소스를 찾을 수 없습니다.");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateException(DuplicateException e,
        HttpServletRequest request) {
        log.warn("Duplicate Exception: message={}, path={}",
            e.getMessage(), request.getRequestURI());

        Map<String, String> response = new HashMap<>();
        response.put("message", "이미 사용 중인 리소스입니다.");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(
        MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult()
            .getAllErrors()
            .getFirst()
            .getDefaultMessage();

        Map<String, String> response = new HashMap<>();
        response.put("message", errorMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
