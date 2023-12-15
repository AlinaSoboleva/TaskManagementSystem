package ru.taskManagement.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEntityNotFoundException(EntityNotFoundException e) {
        log.debug("Получен статус 404 NOT FOUND: {}", e.getMessage(), e);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .reason("Требуемый объект не найден")
                .message(e.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .build();

        log.warn(errorResponse.toString());
        return errorResponse;
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictExeption(ConflictException e) {
        log.debug("Получен статус 409 CONFLICT: {}", e.getMessage(), e);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .reason("Ограничение целостности было нарушено")
                .message(e.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .build();

        log.warn(errorResponse.toString());
        return errorResponse;
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, HttpRequestMethodNotSupportedException.class,
            MissingServletRequestParameterException.class, HttpMessageNotReadableException.class, IllegalArgumentException.class,
            ConstraintViolationException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(final Exception e) {
        log.debug("Получен статус 400 BAD REQUEST: {}", e.getMessage(), e);
        ErrorResponse response = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("Incorrectly made request.")
                .message(e.getMessage())
                .build();

        log.warn(response.toString());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(final AuthenticationCredentialsNotFoundException e) {
        log.debug("Получен статус 401 UNAUTHORIZED: {}", e.getMessage(), e);
        ErrorResponse response = ErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .reason("Ошибка аутентификации.")
                .message(e.getMessage())
                .build();

        log.warn(response.toString());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleThrowable(final Throwable e) {
        log.debug("Получен статус 500 INTERNAL SERVER ERROR: {}", e.getMessage(), e);
        ErrorResponse response = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .reason("Произошла непредвиденная ошибка.")
                .message(e.getMessage())
                .build();

        log.warn(response.toString());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
