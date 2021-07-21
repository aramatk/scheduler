package com.scheduler.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler extends AbstractExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ApiError handleResponseStatusException(ResponseStatusException exception, HttpServletRequest request, HttpServletResponse response) {
        return setResponseStatusAndReturnError(exception, exception.getReason(), exception.getStatus(), request, response);
    }

    @ExceptionHandler(SchedulerServiceException.class)
    public ApiError handleAll(SchedulerServiceException exception, HttpServletRequest request, HttpServletResponse response) {
        return setResponseStatusAndReturnError(exception, "scheduler-service-exception", HttpStatus.INTERNAL_SERVER_ERROR, request, response);
    }

    @ExceptionHandler(Exception.class)
    public ApiError handleAll(Exception exception, HttpServletRequest request, HttpServletResponse response) {
        log.error("Unhandled error", exception);
        return setResponseStatusAndReturnError(exception, "internal-error", HttpStatus.INTERNAL_SERVER_ERROR, request, response);
    }
}
