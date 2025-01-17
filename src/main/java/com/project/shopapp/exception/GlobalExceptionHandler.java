package com.project.shopapp.exception;

import com.project.shopapp.dtos.ErrorDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setStatusError(HttpStatus.NOT_FOUND);
        errorDetails.setErrorMessage(ex.getMessage());
        errorDetails.setApiPath(webRequest.getDescription(false));
        errorDetails.setErrorTime(LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(PhoneNumberAlreadyExistsException.class)
    public ResponseEntity<ErrorDetails> handlePhoneNumberAlreadyExistsException(PhoneNumberAlreadyExistsException ex, WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setStatusError(HttpStatus.BAD_REQUEST);
        errorDetails.setErrorMessage(ex.getMessage());
        errorDetails.setApiPath(webRequest.getDescription(false));
        errorDetails.setErrorTime(LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ImageException.class)
    public ResponseEntity<ErrorDetails> handleImageException(ImageException ex, WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setStatusError(HttpStatus.BAD_REQUEST);
        errorDetails.setErrorMessage(ex.getMessage());
        errorDetails.setApiPath(webRequest.getDescription(false));
        errorDetails.setErrorTime(LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorDetails> handleUserException(UserException ex, WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setStatusError(HttpStatus.BAD_REQUEST);
        errorDetails.setErrorMessage(ex.getMessage());
        errorDetails.setApiPath(webRequest.getDescription(false));
        errorDetails.setErrorTime(LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorDetails> handleJwtException(JwtException ex, WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setStatusError(HttpStatus.BAD_REQUEST);
        errorDetails.setErrorMessage(ex.getMessage());
        errorDetails.setApiPath(webRequest.getDescription(false));
        errorDetails.setErrorTime(LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<ErrorDetails> handleOrderException(OrderException ex, WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setStatusError(HttpStatus.INTERNAL_SERVER_ERROR);
        errorDetails.setErrorMessage(ex.getMessage());
        errorDetails.setApiPath(webRequest.getDescription(false));
        errorDetails.setErrorTime(LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String,String> errors = new HashMap<>();
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        allErrors.forEach(error -> {
            String fieldName = ((FieldError)error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName,message);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception ex, WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setStatusError(HttpStatus.INTERNAL_SERVER_ERROR);
        errorDetails.setErrorMessage(ex.getMessage());
        errorDetails.setApiPath(webRequest.getDescription(false));
        errorDetails.setErrorTime(LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
