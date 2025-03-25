package com.epam.nosql.search.web.controller;

import com.epam.nosql.search.facade.impl.BookingFacadeImpl;
import com.epam.nosql.search.model.dto.UserRequestDto;
import com.epam.nosql.search.model.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@Validated
@Controller
@RequestMapping("/users")
public class UsersController {

    private final BookingFacadeImpl bookingFacade;

    @Autowired
    public UsersController(BookingFacadeImpl bookingFacade) {
        this.bookingFacade = bookingFacade;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> showUserById(
            @NotNull(message = "Id is not provided") @PathVariable String id) {
        log.info("Showing user by id: {}", id);
        User userById = bookingFacade.getUserById(id);
        log.info("The user by id: {} successfully found", id);
        return ResponseEntity.ok(Map.of("user", new UserRequestDto(userById.getId(), userById.getName(), userById.getEmail())));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Map<String, Object>> showUsersByName(
            @NotNull(message = "Name is not provided") @PathVariable String name,
            @NotNull(message = "pageSize is not provided") @RequestParam int pageSize,
            @NotNull(message = "pageNum is not provided") @RequestParam int pageNum) {
        log.info("Showing users by name: {}", name);
        List<UserRequestDto> usersByName = bookingFacade.getUsersByName(name, pageSize, pageNum).stream()
                .map(this::createDto)
                .toList();
        if (usersByName.isEmpty()) {
            log.info("Can not to find users by name: {}", name);
            throw new NoSuchElementException("Can not to find users by name: " + name);
        } else {
            log.info("The users by name: {} successfully found", name);
            return ResponseEntity.ok(Map.of("users", usersByName));
        }

    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Map<String, Object>> showUserByEmail(@PathVariable String email) {
        log.info("Showing the user by email: {}", email);
        User userByEmail = bookingFacade.getUserByEmail(email);
        log.info("The user by email: {} successfully found", email);
        return ResponseEntity.ok(Map.of("user", createDto(userByEmail)));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(
            @NotNull(message = "Name is not provided") @RequestParam String name,
            @NotNull(message = "Email is not provided") @RequestParam String email) {
        log.info("Creating user with name={} and email={}", name, email);
        User user = bookingFacade.createUser(new UserRequestDto(name, email));
        return ResponseEntity.ok(Map.of("user", createDto(user)));
    }

    @PutMapping
    public ResponseEntity<Map<String, Object>> updateUser(
            @NotNull(message = "Id is not provided") @RequestParam String id,
            @NotNull(message = "Name is not provided") @RequestParam String name,
            @NotNull(message = "Email is not provided") @RequestParam String email) {
        log.info("Updating user with id: {}", id);
        User user = bookingFacade.updateUser(new UserRequestDto(
                id,
                name,
                email
        ));
        return ResponseEntity.ok(Map.of("user", createDto(user)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable String id) {
        log.info("Deleting the user with id: {}", id);
        bookingFacade.deleteUser(id);
        log.info("The user with id: {} successfully removed", id);
        return ResponseEntity.ok(Map.of("user", id));
    }

    private UserRequestDto createDto(User user){
        return new UserRequestDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
