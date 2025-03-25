package com.epam.nosql.search.model.exceptions;

public class NotFoundDocumentException extends RuntimeException {
    public NotFoundDocumentException(String message) {
        super(message);
    }
}
