package com.gabrielferreira02.springmail.service.implementation;

import com.gabrielferreira02.springmail.persistence.entity.User;
import com.gabrielferreira02.springmail.persistence.repository.ChatRepository;
import com.gabrielferreira02.springmail.persistence.repository.FavoriteRepository;
import com.gabrielferreira02.springmail.persistence.repository.MessageRepository;
import com.gabrielferreira02.springmail.persistence.repository.UserRepository;
import com.gabrielferreira02.springmail.presentation.dto.UpdatePasswordDTO;
import com.gabrielferreira02.springmail.presentation.dto.UpdateUsernameDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ChatRepository chatRepository;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;

    @Nested
    class GetUserByEmail {

        @Test
        @DisplayName("It should return an existent user successfully")
        void success() {
            User user = new User(null, "user", "user@springmail.com", "12345678");

            when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

            User response = userService.getUserByEmail(user.getEmail());

            assertNotNull(response);
            assertEquals("user", response.getUsername());
            assertEquals("user@springmail.com", response.getEmail());

        }

        @Test
        @DisplayName("It should return null for user not found")
        void error() {
            String email = "example@springmail.com";

            when(userRepository.findByEmail(email)).thenReturn(null);

            User response = userService.getUserByEmail(email);

            assertNull(response);
        }
    }

    @Nested
    class UpdateUsername {

        @Test
        @DisplayName("It should update username successfully")
        void success() {
            User user = new User(null, "user", "user@springmail.com", "12345678");

            UpdateUsernameDTO body = new UpdateUsernameDTO("user modified", "user@springmail.com");

            when(userRepository.findByEmail(body.email())).thenReturn(user);

            ResponseEntity<Void> response = userService.updateUsername(body);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        @DisplayName("It should fail on update the username because its empty")
        void errorCase1() {
            UpdateUsernameDTO body = new UpdateUsernameDTO("", "user@springmail.com");

            ResponseEntity<Void> response = userService.updateUsername(body);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }

        @Test
        @DisplayName("It should fail on update the username because user doesn't exists")
        void errorCase2() {
            UpdateUsernameDTO body = new UpdateUsernameDTO("user modified", "usererror@springmail.com");

            when(userRepository.findByEmail(body.email())).thenReturn(null);

            ResponseEntity<Void> response = userService.updateUsername(body);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    class UpdatePassword {
        @Test
        @DisplayName("It should update password successfully")
        void success() {
            User user = new User(null, "user", "user@springmail.com", "12345678");

            UpdatePasswordDTO body = new UpdatePasswordDTO("123456789", "12345678", "user@springmail.com");

            when(userRepository.findByEmail(body.email())).thenReturn(user);

            ResponseEntity<?> response = userService.updatePassword(body);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        @DisplayName("It should fail on update the password because its less than 8 characters")
        void errorCase1() {
            UpdatePasswordDTO body = new UpdatePasswordDTO("1234567", "12345678", "user@springmail.com");

            ResponseEntity<?> response = userService.updatePassword(body);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }

        @Test
        @DisplayName("It should fail on update the password because user doesn't exists")
        void errorCase2() {
            UpdatePasswordDTO body = new UpdatePasswordDTO("123456789", "12345678", "usererror@springmail.com");

            when(userRepository.findByEmail(body.email())).thenReturn(null);

            ResponseEntity<?> response = userService.updatePassword(body);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    class deleteUser {

        @Test
        @DisplayName("It should delete a user successfully")
        void success() {
            User user = new User(null, "user", "user@springmail.com", "12345678");

            when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

            ResponseEntity<Void> response = userService.deleteUser(user.getEmail());

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        }

        @Test
        @DisplayName("It should fail on delete a user")
        void error() {

            when(userRepository.findByEmail("usererror@springmail.com")).thenReturn(null);

            ResponseEntity<Void> response = userService.deleteUser("usererror@springmail.com");

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }
}