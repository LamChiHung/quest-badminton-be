package com.quest.badminton.controller.handler;

import com.quest.badminton.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.quest.badminton.constant.ErrorConstants.ERR_INTERNAL_SERVER;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity <ErrorResponse> handleAccessDenied(BadRequestException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .errorCode(ex.getMessage())
                        .errorMessage(ex.getMessage())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity <ErrorResponse> handleAccessDenied(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder()
                        .errorCode(ERR_INTERNAL_SERVER)
                        .errorMessage(ERR_INTERNAL_SERVER)
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .build());
    }
}
