package com.epam.nosql.search.service;

import com.epam.nosql.search.dto.UserRequestDto;
import com.epam.nosql.search.dto.entity.User;

import java.util.List;

public interface UserService {
    User createUser(UserRequestDto user);

    User getUserById(String id);

    User getUserByEmail(String email);

    List<User> getUsersByName(String name, int pageSize, int pageNum);

    User updateUser(UserRequestDto userRequestDto);
}
