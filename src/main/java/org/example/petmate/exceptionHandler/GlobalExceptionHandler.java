package org.example.petmate.exceptionHandler;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.example.petmate.dto.NotFoundErrorDto;
import org.example.petmate.dto.ValidationErrorResponse;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> validationErrorResponseResponseEntity(
            MethodArgumentNotValidException ex
    ) {
        List<ValidationErrorResponse.FieldError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream().map(error -> new ValidationErrorResponse.FieldError(
                        error.getField(),
                        error.getDefaultMessage(),
                        error.getRejectedValue()
                )).toList();

        String message = ex.getBindingResult().getFieldErrors()
                .stream().map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ValidationErrorResponse validationErrorResponse = new ValidationErrorResponse(message, fieldErrors);

        log.info("Got field error: {}", message);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(validationErrorResponse);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<NotFoundErrorDto> handleNotFoundError(
            NoSuchElementException ex
    ) {
        log.info("Got no such element exception ", ex);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new NotFoundErrorDto(
                        "Not found error",
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }
}
