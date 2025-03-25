package com.epam.nosql.search.model;

public record SimpleErrorResponse(
        String errorMessage,
        String errorCode
) {}
