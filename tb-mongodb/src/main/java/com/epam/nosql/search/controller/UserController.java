package com.epam.nosql.search.controller;

import com.epam.nosql.search.dto.UserRequestDto;
import com.epam.nosql.search.dto.entity.User;
import com.epam.nosql.search.facade.BookingFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {

    private final BookingFacade bookingFacade;

    public record PostRequestBody(String name, String email){}

    @Autowired
    public UserController(BookingFacade bookingFacade) {
        this.bookingFacade = bookingFacade;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> showUserById(@PathVariable String id) {
        log.info("Showing user by id: {}", id);
        Map<String, Object> model = new HashMap<>();
        User userById = bookingFacade.getUserById(id);
        if (isNull(userById)) {
            model.put("message", "Can not to find user by id: " + id);
            log.info("Can not to find user by id: {}", id);
        } else {
            model.put("user", userById);
            log.info("The user by id: {} successfully found", id);
        }
        return ResponseEntity.ok().body(model);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody UserRequestDto body) {
        log.info("Creating user with name={} and email={}", body.name(), body.email());
        var model = new HashMap<String, Object>();
        User user = bookingFacade.createUser(body);
        if (isNull(user)) {
            model.put("message",
                    "Can not to create user with name - " + body.name() + " and email - " + body.email());
            log.info("Can not to create user with name={} and email={}", body.name(), body.email());
        } else {
            model.put("user", user);
            log.info("The user successfully created");
        }
        return ResponseEntity.ok().body(model);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Map<String, Object>> showUserByEmail(@PathVariable String email) {
        log.info("Showing the user by email: {}", email);
        Map<String, Object> model = new HashMap<>();
        User userByEmail = bookingFacade.getUserByEmail(email);
        if (isNull(userByEmail)) {
            model.put("message", "Can not to find user by email: " + email);
            log.info("Can not to find user by email: {}", email);
        } else {
            model.put("user", userByEmail);
            log.info("The user by email: {} successfully found", email);
        }
        return ResponseEntity.ok().body(model);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Map<String, Object>> showUsersByName(@PathVariable String name,
                                        @RequestParam int pageSize,
                                        @RequestParam int pageNum) {
        log.info("Showing users by name: {}", name);
        Map<String, Object> model = new HashMap<>();
        List<User> usersByName = bookingFacade.getUsersByName(name, pageSize, pageNum);
        if (usersByName.isEmpty()) {
            model.put("message", "Can not to find users by name: " + name);
            log.info("Can not to find users by name: {}", name);
        } else {
            model.put("users", usersByName);
            log.info("The users by name: {} successfully found", name);
        }
        return ResponseEntity.ok().body(model);
    }

    @PutMapping
    public ResponseEntity<Map<String, Object>> updateUser(@RequestParam String id,
                                   @RequestParam String name,
                                   @RequestParam String email) {
        log.info("Updating user with id: {}", id);
        Map<String, Object> model = new HashMap<>();
        User user = bookingFacade.updateUser(new UserRequestDto(id, name, email));
        if (isNull(user)) {
            model.put("message", "Can not to update user with id: " + id);
            log.info("Can not to update user with id: {}", id);
        } else {
            model.put("user", user);
            log.info("The user with id: {} successfully update", id);
        }
        return ResponseEntity.ok().body(model);
    }
}
