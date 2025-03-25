package com.epam.nosql.search.service.impl;

import com.epam.nosql.search.model.dto.UserRequestDto;
import com.epam.nosql.search.model.entity.User;
import com.epam.nosql.search.model.exceptions.ConflictWithExistingDataException;
import com.epam.nosql.search.model.exceptions.NotFoundDocumentException;
import com.epam.nosql.search.repository.UserRepository;
import com.epam.nosql.search.service.UserService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUserById(@NonNull String id) {
        log.info("Finding a user by id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundDocumentException("Can not to get a user by id: " + id));
        log.info("The user with id {} successfully found ", id);
        return user;
    }

    @Override
    public List<User> getUsersByName(@NonNull String name, int pageSize, int pageNum) {
        log.info("Finding all users by name {} with page size {} and number of page {}", name, pageSize, pageNum);
        Page<User> usersByName = userRepository.findByName(name, PageRequest.of(pageNum - 1, pageSize));
        if (!usersByName.hasContent()) {
            log.warn("Can not to find a list of users by name '{}'", name);
            return List.of();
        }
        log.info("All users successfully found by name {} with page size {} and number of page {}",
                name, pageSize, pageNum);
        return usersByName.getContent();
    }

    @Override
    public User getUserByEmail(@NonNull String email) {
        log.info("Finding a user by email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundDocumentException("Can not to get an user by email: " + email));
        log.info("The user with email {} successfully found ", email);
        return user;
    }


    @Override
    public User createUser(@NonNull UserRequestDto userDto) {
        log.info("Start creating an user: {}", userDto);

        checkEmail(userDto.email(), null);

        var savedUser = userRepository.save(new User(userDto.name(), userDto.email()));
        log.info("Successfully created user: {}", savedUser);
        return savedUser;
    }


    @Override
    public User updateUser(@NonNull UserRequestDto userDto) {
        log.info("Start updating an user: {}", userDto);
        var user = getUserById(userDto.id());

        checkEmail(userDto.email(), user.getEmail());

        user.setEmail(userDto.email());
        user.setName(userDto.name());

        var updatedUser = userRepository.save(user);
        log.info("Successfully updating of the user: {}", user);
        return updatedUser;
    }

    @Override
    public void deleteUser(@NonNull String userId) {
        userRepository.deleteById(userId);
    }

    private void checkEmail(@NonNull String newEmail, String oldEmail) {
        if ((!newEmail.equals(oldEmail)) && userRepository.existsByEmail(newEmail)) {
            throw new ConflictWithExistingDataException("This email already exists");
        }
    }

    @Override
    public User refillAccount(@NonNull String userId, @NonNull BigDecimal money) {
        thrownRuntimeExceptionIfMoneyLessZero(money);
        throwRuntimeExceptionIfUserNotExist(userId);
        User user = getUserAccountAndRefillIfNotExistCreate(userId, money);
        User updatedUser = userRepository.save(user);
        log.info("The user account with user id {} successfully refilled", userId);
        return updatedUser;
    }

    private void thrownRuntimeExceptionIfMoneyLessZero(BigDecimal money) {
        if (money.compareTo(BigDecimal.ZERO) < 1) {
            throw new RuntimeException("The money can not to be less zero");
        }
    }

    private void throwRuntimeExceptionIfUserNotExist(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("The user with id " + userId + " does not exist");
        }
    }

    private User getUserAccountAndRefillIfNotExistCreate(String userId, BigDecimal money) {
        User user = getUserById(userId);
        BigDecimal money1 = user.getMoney();
        user.setMoney(money1.add(money));
        return user;
    }
}
