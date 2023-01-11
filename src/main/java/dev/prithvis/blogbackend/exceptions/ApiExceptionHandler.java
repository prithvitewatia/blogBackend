package dev.prithvis.blogbackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFound(Exception exception, WebRequest webRequest) {
        return new ResponseEntity<>(Map.of(
                "message", exception.getMessage(),
                "path", ((ServletWebRequest) webRequest).getRequest().getRequestURI(),
                "success", "false"),
                HttpStatus.NOT_FOUND);
    }
}
