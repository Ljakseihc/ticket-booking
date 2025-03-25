package com.epam.nosql.search.web.handler;

import com.epam.nosql.search.model.SimpleErrorResponse;
import com.epam.nosql.search.model.exceptions.ConflictWithExistingDataException;
import com.epam.nosql.search.model.exceptions.NotFoundDocumentException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ErrorHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public SimpleErrorResponse handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        return new SimpleErrorResponse(
                "The request body is invalid MP3",
                "400"
        );
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public SimpleErrorResponse handleConstraintViolationException(ConstraintViolationException ex) {
        return new SimpleErrorResponse(
                ex.getConstraintViolations().iterator().next().getMessage(),
                "400"
        );
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public SimpleErrorResponse handleIOException(NoSuchElementException ex) {
        return new SimpleErrorResponse(
                "The resource with the specified id does not exist",
                "404"
        );
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundDocumentException.class)
    public SimpleErrorResponse handleConflictWithExistingResourceException(NotFoundDocumentException ex) {
        return new SimpleErrorResponse(
                ex.getMessage(),
                "404"
        );
    }

//    @ResponseBody
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler(RuntimeException.class)
//    public SimpleErrorResponse handleRuntimeException(RuntimeException ex) {
//        return new SimpleErrorResponse(
//                "An internal server error has occurred",
//                "500"
//        );
//    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public SimpleErrorResponse handleGenericException(Exception ex) {
        return new SimpleErrorResponse(
                "An internal server error has occurred",
                "500"
        );
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictWithExistingDataException.class)
    public SimpleErrorResponse handleConflictWithExistingResourceException(ConflictWithExistingDataException ex) {
        return new SimpleErrorResponse(
                ex.getMessage(),
                "409"
        );
    }
}
