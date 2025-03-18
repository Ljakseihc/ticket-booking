package com.epam.nosql.search.facade.impl;

import com.epam.nosql.search.dto.UserRequestDto;
import com.epam.nosql.search.dto.entity.User;
import com.epam.nosql.search.facade.BookingFacade;
import com.epam.nosql.search.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookingFacadeImpl implements BookingFacade {

    private final UserService userService;

    @Autowired
    public BookingFacadeImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User createUser(UserRequestDto user) {
        return userService.createUser(user);
    }

    @Override
    public User getUserById(String id) {
        return userService.getUserById(id);
    }

    @Override
    public User getUserByEmail(String email) {
        return userService.getUserByEmail(email);
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        return userService.getUsersByName(name, pageSize, pageNum);
    }

    @Override
    public User updateUser(UserRequestDto userRequestDto) {
        return userService.updateUser(userRequestDto);
    }
}
