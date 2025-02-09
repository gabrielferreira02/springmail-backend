package com.gabrielferreira02.springmail.service.implementation;

import com.gabrielferreira02.springmail.persistence.entity.User;
import com.gabrielferreira02.springmail.persistence.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private CustomUserDetailServiceImpl customUserDetailService;

    @Nested
    class LoadUserByUsername {

        @Test
        @DisplayName("It should return a user details with sucess")
        void success() {
            User user = new User(
                    null,
                    "user",
                    "user@springmail.com",
                    "12345678"
            );

            when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

            UserDetails response = customUserDetailService.loadUserByUsername(user.getEmail());

            assertNotNull(response);
            assertEquals("user@springmail.com", response.getUsername());
        }

        @Test
        @DisplayName("It should throw a exception")
        void error() {

            when(userRepository.findByEmail("error@springmail.com")).thenReturn(null);

            assertThrows(UsernameNotFoundException.class, () -> {
                customUserDetailService.loadUserByUsername("error@springmail.com");
            });

        }
    }

}