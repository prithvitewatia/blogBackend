package dev.prithvis.blogbackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFound(EntityNotFoundException exception,
                                                                    WebRequest webRequest) {
        Map<String,Object> errorsMap=Map.of("message", exception.getMessage(),
                "path",((ServletWebRequest) webRequest).getRequest().getRequestURI(),
                "success",false);
        return new ResponseEntity<>(errorsMap,
                HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
                                                                                    WebRequest webRequest){
        Map<String,String> errorMessages=new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(objectError -> {
            String fieldName=((FieldError)objectError).getField();
            errorMessages.put(fieldName,
                    objectError.getDefaultMessage());
        });
        return new ResponseEntity<>(Map.of(
                "path",((ServletWebRequest)webRequest).getRequest().getRequestURI(),
                "success",false,
                "count",errorMessages.size(),
                "errors",errorMessages
        ),HttpStatus.BAD_REQUEST);
    }
}
