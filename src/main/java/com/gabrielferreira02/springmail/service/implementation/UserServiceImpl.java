package com.gabrielferreira02.springmail.service.implementation;

import com.gabrielferreira02.springmail.persistence.entity.User;
import com.gabrielferreira02.springmail.persistence.repository.ChatRepository;
import com.gabrielferreira02.springmail.persistence.repository.FavoriteRepository;
import com.gabrielferreira02.springmail.persistence.repository.MessageRepository;
import com.gabrielferreira02.springmail.persistence.repository.UserRepository;
import com.gabrielferreira02.springmail.presentation.dto.UpdatePasswordDTO;
import com.gabrielferreira02.springmail.presentation.dto.UpdateUsernameDTO;
import com.gabrielferreira02.springmail.service.interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final FavoriteRepository favoriteRepository;

    public UserServiceImpl(UserRepository userRepository, ChatRepository chatRepository, MessageRepository messageRepository, FavoriteRepository favoriteRepository) {
        this.userRepository = userRepository;
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);

        if(user == null) {
            log.error("User not found: {}", email);
            return null;
        }

        log.info("User details returned for: {}", email);
        return user;
    }

    @Override
    public ResponseEntity<Void> updateUsername(UpdateUsernameDTO body) {
        User user = userRepository.findByEmail(body.email());

        if(user == null) {
            log.error("User not found: {}", body.email());
            return null;
        }

        user.setUsername(body.username());
        userRepository.save(user);
        log.info("Username updated with success");
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<?> updatePassword(UpdatePasswordDTO body) {
        User user = userRepository.findByEmail(body.email());

        if(user == null) {
            log.error("User not found: {}", body.email());
            return ResponseEntity.notFound().build();
        }

        if(!user.getPassword().equals(body.oldPassword())) {
            return ResponseEntity.badRequest().body("Senha incorreta");
        }

        user.setPassword(body.newPassword());
        userRepository.save(user);
        log.info("Password updated with success");
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<Void> deleteUser(String email) {
        User user = userRepository.findByEmail(email);

        if(user == null) {
            log.error("User not found: {}", email);
            return ResponseEntity.notFound().build();
        }

        messageRepository.deleteByChatFromUser(user.getId());
        favoriteRepository.deleteFavoritesByChat(user.getId());
        chatRepository.deleteByToId(user.getId());
        chatRepository.deleteByFromId(user.getId());
        userRepository.deleteById(user.getId());
        log.info("User {} deleted with success", user.getEmail());
        return ResponseEntity.noContent().build();

    }
}
