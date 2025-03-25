package com.epam.nosql.search.model.dto;

public record UserRequestDto(String id, String name, String email){
    public UserRequestDto(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public UserRequestDto(String name, String email) {
        this(null, name, email);
    }
}
