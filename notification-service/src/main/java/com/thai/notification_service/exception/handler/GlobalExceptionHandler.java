package com.thai.notification_service.exception.handler;

import com.thai.notification_service.dto.ResponseObject;
import com.thai.notification_service.exception.BaseException;
import com.thai.notification_service.exception.black.BlackException;
import com.thai.notification_service.exception.white.WhiteException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({Exception.class, BlackException.class})
    ResponseEntity<ResponseObject<ExceptionDTO>> unexpectExceptionHandler(
            Exception e
    ) {
        log.error(String.valueOf(e));
        e.printStackTrace(System.err);

        ResponseObject<ExceptionDTO> body = ResponseObject.error(
                new ExceptionDTO(BaseException.createCode(), BaseException.defaultMsg)
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseObject<ExceptionDTO>> accessDeniedHandler(
            AccessDeniedException e
    ) {
        log.warn(String.valueOf(e));

        ResponseObject<ExceptionDTO> body = ResponseObject.error(
                new ExceptionDTO("AUTH_WH_ADE", "Current user doesn't have enough permission")
        );
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(body);
    }

    // Handle validation error
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        List<String> errorList = e
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        ResponseObject<ExceptionDTO> body = ResponseObject.error(new ExceptionDTO("Bad request", errorList));
        return this.handleExceptionInternal(e, body, headers, status, request);
    }

    @ExceptionHandler
    ResponseEntity<ResponseObject<ExceptionDTO>> expectExceptionHandler(
            WhiteException e
    ) {
        log.warn(String.valueOf(e));

        ResponseObject<ExceptionDTO> body = ResponseObject.error(new ExceptionDTO(e.getCode(), e.getMessage()));
        return ResponseEntity
                .status(e.getStatus())
                .body(body);
    }
}
