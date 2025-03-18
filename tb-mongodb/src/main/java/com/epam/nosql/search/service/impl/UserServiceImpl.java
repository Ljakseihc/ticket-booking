package com.epam.nosql.search.service.impl;

import com.epam.nosql.search.dto.UserRequestDto;
import com.epam.nosql.search.dto.entity.User;
import com.epam.nosql.search.repository.UserRepository;
import com.epam.nosql.search.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(UserRequestDto userDto) {
        log.info("Start creating an user: {}", userDto);
        try {
            if (Objects.isNull(userDto)) {
                log.warn("The user can not be a null");
                return null;
            }
            return getUser(userDto);
        } catch (RuntimeException e) {
            log.warn("Can not to create an user: {}", userDto, e);
            return null;
        }
    }

    private User getUser(UserRequestDto userDto) {
        return userRepository.findByEmail(userDto.email())
                .map(existingUser -> {
                    log.debug("This email already exists");
                    return existingUser;
                })
                .orElseGet(() -> {
                    var savedUser = userRepository.save(new User(userDto.name(), userDto.email()));
                    log.info("Successfully created user: {}", savedUser);
                    return savedUser;
                });
    }

    @Override
    public User getUserById(String id) {
        log.info("Finding a user by id: {}", id);
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Can not to get a user by id: " + id));
            log.info("The user with id {} successfully found ", id);
            return user;
        } catch (RuntimeException e) {
            log.warn("Can not to get an user by id: {}", id);
            return null;
        }
    }

    @Override
    public User getUserByEmail(String email) {
        log.info("Finding a user by email: {}", email);
        try {
            if (email.isEmpty()) {
                log.warn("The email can not be null");
                return null;
            }
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Can not to get an user by email: " + email));
            log.info("The user with email {} successfully found ", email);
            return user;
        } catch (RuntimeException e) {
            log.warn("Can not to get an user by email: {}", email);
            return null;
        }
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        log.info("Finding all users by name {} with page size {} and number of page {}", name, pageSize, pageNum);
        try {
            if (name.isEmpty()) {
                log.warn("The name can not be null");
                return List.of();
            }
            Page<User> usersByName = userRepository.findByName(name, PageRequest.of(pageNum - 1, pageSize));
            if (!usersByName.hasContent()) {
                log.warn("Can not to find a list of users by name '{}'", name);
            }
            log.info("All users successfully found by name {} with page size {} and number of page {}",
                    name, pageSize, pageNum);
            return usersByName.getContent();
        } catch (RuntimeException e) {
            log.warn("Can not to find a list of users by name '{}'", name, e);
            return List.of();
        }
    }

    @Override
    public User updateUser(UserRequestDto userDto) {
        log.info("Start updating an user: {}", userDto);
        try {
            if (userDto == null) {
                log.warn("The user can not be a null");
                return null;
            }

            var user = userRepository.findById(userDto.id())
                    .orElseThrow(() -> new RuntimeException("This user does not exist"));

            if (userExistsByEmail(userDto)) {
                throw new RuntimeException("This email already exists");
            }
            user.setEmail(user.getEmail());
            user.setName(user.getName());
            var updatedUser = userRepository.save(user);
            log.info("Successfully updating of the user: {}", user);
            return updatedUser;
        } catch (RuntimeException e) {
            log.warn("Can not to update an user: {}", userDto, e);
            return null;
        }
    }

    private boolean userExistsById(User user) {
        return userRepository.existsById(user.getId());
    }

    private boolean userExistsByEmail(UserRequestDto userDto) {
        return userRepository.existsByEmail(userDto.email());
    }


}
