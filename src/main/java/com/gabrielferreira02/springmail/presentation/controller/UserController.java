package com.gabrielferreira02.springmail.presentation.controller;

import com.gabrielferreira02.springmail.persistence.entity.User;
import com.gabrielferreira02.springmail.presentation.dto.UpdatePasswordDTO;
import com.gabrielferreira02.springmail.presentation.dto.UpdateUsernameDTO;
import com.gabrielferreira02.springmail.service.implementation.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @PutMapping("username")
    public ResponseEntity<Void> updateUsername(@RequestBody UpdateUsernameDTO body) {
        return userService.updateUsername(body);
    }

    @PutMapping("password")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordDTO body) {
        return userService.updatePassword(body);
    }

    @DeleteMapping("{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable String email) {
        return userService.deleteUser(email);
    }
}
