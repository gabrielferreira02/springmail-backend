package com.gabrielferreira02.springmail.persistence.repository;

import com.gabrielferreira02.springmail.persistence.entity.Chat;
import com.gabrielferreira02.springmail.presentation.dto.ChatDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {
    @Query("""
          SELECT new com.gabrielferreira02.springmail.presentation.dto.ChatDTO(
              c.id, c.subject, c.from.username,
              m.content, c.isRead, c.updatedAt
          ) \s
          FROM Chat c
          LEFT JOIN Message m ON m.chat.id = c.id
          WHERE c.to.id = :userId
          AND m.createdAt = (SELECT MAX(m2.createdAt) FROM Message m2 WHERE m2.chat.id = c.id)
          ORDER BY c.updatedAt DESC
    """)
    List<ChatDTO> getAllChatsByUserId(UUID userId, Pageable pageable);

}
