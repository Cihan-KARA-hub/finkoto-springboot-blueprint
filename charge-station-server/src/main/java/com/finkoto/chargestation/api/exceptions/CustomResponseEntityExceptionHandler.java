package com.finkoto.chargestation.api.exceptions;


import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.ServiceUnavailableException;
import jakarta.ws.rs.core.NoContentException;
import org.apache.http.auth.AuthenticationException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * 204 ->  No Content
 * 304 ->  Not Modified
 * 400 ->  Bad Request
 * 401 ->  Unauthorized
 * 402 ->  Payment Required
 * 403 ->  Forbidden
 * 404 ->  Not Found
 * 405 ->  Method Not Allowed
 * 406 ->  Not Acceptable
 * 408 ->  The Request Timeout
 * 409 ->  Conflict
 * 500 ->  Internal Server Error
 * 503 ->  No Service
 * 504 ->  Gateway Timeout
 */
@RestControllerAdvice
public class CustomResponseEntityExceptionHandler {


    @ExceptionHandler(NoContentException.class)
    public final ResponseEntity<Object> handleNoContentException(NoContentException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "No content available.");
        errors.put("details", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<Object> handleBadRequestException(BadRequestException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "Bad request.");
        errors.put("details", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public final ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "Authentication failed.");
        errors.put("details", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenException.class)
    public final ResponseEntity<Object> handleForbiddenException(ForbiddenException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "Access is forbidden.");
        errors.put("details", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    public final ResponseEntity<Object> handleNotFoundException(ChangeSetPersister.NotFoundException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "The requested resource was not found.");
        errors.put("details", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public final ResponseEntity<Object> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "The HTTP method is not allowed for this resource.");
        errors.put("details", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpClientErrorException.NotAcceptable.class)
    public final ResponseEntity<Object> handleNotAcceptable(HttpClientErrorException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "The requested resource is not acceptable.");
        errors.put("details", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.NOT_ACCEPTABLE);
    }
    @ExceptionHandler(HttpClientErrorException.Conflict.class)
    public final ResponseEntity<Object> handleConflictException(HttpClientErrorException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "The requested resource was not found.");
        errors.put("details", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "An unexpected error occurred.");
        errors.put("details", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public final ResponseEntity<Object> handleServiceUnavailableException(ServiceUnavailableException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "The service is currently unavailable.");
        errors.put("details", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(HttpServerErrorException.GatewayTimeout.class)
    public final ResponseEntity<Object> handleGatewayTimeout(HttpServerErrorException.GatewayTimeout ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "The gateway timed out while processing the request.");
        errors.put("details", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.GATEWAY_TIMEOUT);
    }

}