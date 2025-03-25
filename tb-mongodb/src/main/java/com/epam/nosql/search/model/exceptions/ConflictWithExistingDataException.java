package com.epam.nosql.search.model.exceptions;

public class ConflictWithExistingDataException extends RuntimeException {
    public ConflictWithExistingDataException(String message) {
        super(message);
    }
}
