package com.gabrielferreira02.springmail.service.interfaces;

import com.gabrielferreira02.springmail.persistence.entity.User;
import com.gabrielferreira02.springmail.presentation.dto.UpdatePasswordDTO;
import com.gabrielferreira02.springmail.presentation.dto.UpdateUserDTO;
import com.gabrielferreira02.springmail.presentation.dto.UpdateUsernameDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

public interface UserService {
    User getUserByEmail(String email);
    ResponseEntity<Void> updateUsername(UpdateUsernameDTO body);
    ResponseEntity<Object> updatePassword(UpdatePasswordDTO body);
    ResponseEntity<Void> deleteUser(String email);
}
