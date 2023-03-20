package com.hyunbenny.mybox.controller;

import com.hyunbenny.mybox.dto.response.ErrorResponse;
import com.hyunbenny.mybox.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ExceptionController{

    /**
     * @Valid를 사용하여 validtaion 체크에서 걸리면 MethodArgumentNotValidException이 발생한다.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);

        ErrorResponse response = ErrorResponse.builder()
                .code("400")
                .message("잘못된 요청입니다.")
                .build();

        for (FieldError field : e.getFieldErrors()) {
            response.addValidation(field.getField(), field.getDefaultMessage());
        }


        return response;
    }

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> customExceptionHandler(CustomException e) {
        log.error(e.getMessage(), e);

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(e.getStatusCode()))
                .message(e.getMessage())
                .validationError(e.getValidation())
                .build();

        ResponseEntity<ErrorResponse> responseEntity = ResponseEntity.status(e.getStatusCode())
                .body(body);

        return responseEntity;
    }

}
