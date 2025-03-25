package com.epam.nosql.search.service;

import com.epam.nosql.search.model.dto.UserRequestDto;
import com.epam.nosql.search.model.entity.User;

import java.math.BigDecimal;
import java.util.List;

public interface UserService {
    User createUser(UserRequestDto userDto);

    User getUserById(String id);

    User getUserByEmail(String email);

    List<User> getUsersByName(String name, int pageSize, int pageNum);

    User updateUser(UserRequestDto userDto);

    void deleteUser(String userId);

    User refillAccount(String userId, BigDecimal money);
}
