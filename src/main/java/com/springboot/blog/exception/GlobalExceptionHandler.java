package com.springboot.blog.exception;

import com.springboot.blog.dto.ErrorDetails;
import com.springboot.blog.dto.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    //handle specific exceptions
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(
            ResourceNotFoundException resourceNotFoundException,
            WebRequest webRequest
    ){
        ErrorDetails errorDetails= new ErrorDetails(new Date(),resourceNotFoundException.getMessage(),webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(BlogAPIException.class)
    public ResponseEntity<ErrorDetails> handleBlogAPIException(BlogAPIException blogAPIException,
                                                               WebRequest webRequest){
        ErrorDetails errorDetailsForBlogAPI = new ErrorDetails(new Date(),blogAPIException.getMessage(),webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetailsForBlogAPI,HttpStatus.BAD_REQUEST);
    }

    //Global Exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleBlogAPIException(Exception exception,
                                                               WebRequest webRequest){
        ErrorDetails errorDetailsForBlogAPI = new ErrorDetails(new Date(),exception.getMessage(),webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetailsForBlogAPI,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        //method 1:
        Map<String,String> errors= new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error)->{
            String fieldName= ((FieldError)error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName,message);
        });
        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
    }

/*    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(MethodArgumentNotValidException exception,
                                                                        WebRequest webRequest){
        //method 2:
        Map<String,String> errors= new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error)->{
            String fieldName= ((FieldError)error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName,message);
        });
        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
    }*/
}
