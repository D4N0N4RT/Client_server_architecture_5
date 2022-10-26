package com.example.practice5.controller;

import com.example.practice5.exception.DuplicateUsernameException;
import com.example.practice5.exception.JwtAuthException;
import com.example.practice5.exception.PasswordCheckException;
import com.example.practice5.exception.PriceValidException;
import com.example.practice5.exception.WrongAuthorityException;
import com.example.practice5.exception.WrongIdException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { WrongIdException.class })
    protected ResponseEntity<Object> handleNotFound(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(),
                HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = { AuthenticationException.class, JwtAuthException.class,
            UsernameNotFoundException.class })
    protected ResponseEntity<Object> handleAuthExceptions(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(),
                HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(value = { WrongAuthorityException.class, PasswordCheckException.class,
            DuplicateUsernameException.class, PriceValidException.class})
    protected ResponseEntity<Object> handleConflictExceptions(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(),
                HttpStatus.CONFLICT, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> errors = result.getFieldErrors();
        StringBuilder error = new StringBuilder("Ошибка валидации, проверьте введенные данные\nОшибка: "
                + errors.get(0).getDefaultMessage());
        for (int i = 1; i < errors.size(); i++) {
            error.append(", ").append(errors.get(i).getDefaultMessage());
        }
        return handleExceptionInternal(ex, error, new HttpHeaders(),
                HttpStatus.BAD_REQUEST, request);
    }
}
