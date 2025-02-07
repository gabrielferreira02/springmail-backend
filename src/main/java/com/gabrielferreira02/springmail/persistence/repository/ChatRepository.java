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
              m.content, c.isRead, c.updatedAt,
              CASE WHEN f.id IS NOT NULL THEN true ELSE false END
          ) \s
          FROM Chat c
          LEFT JOIN Message m ON m.chat.id = c.id
          LEFT JOIN Favorite f ON f.chat.id = c.id AND f.user.id = :userId
          WHERE c.to.id = :userId
          AND m.createdAt = (SELECT MAX(m2.createdAt) FROM Message m2 WHERE m2.chat.id = c.id)
          ORDER BY c.updatedAt DESC
    """)
    List<ChatDTO> getAllChatsByUserId(UUID userId, Pageable pageable);

    @Query("""
          SELECT new com.gabrielferreira02.springmail.presentation.dto.ChatDTO(
              c.id, c.subject, c.from.username,
              m.content, c.isRead, c.updatedAt,
              CASE WHEN f.id IS NOT NULL THEN true ELSE false END
          ) \s
          FROM Chat c
          LEFT JOIN Message m ON m.chat.id = c.id
          LEFT JOIN Favorite f ON f.chat.id = c.id AND f.user.id = :userId
          WHERE c.from.id = :userId
          AND m.createdAt = (SELECT MAX(m2.createdAt) FROM Message m2 WHERE m2.chat.id = c.id)
          ORDER BY c.updatedAt DESC
    """)
    List<ChatDTO> getAllSentChatsByUserId(UUID userId, Pageable pageable);

    @Query("""
            SELECT new com.gabrielferreira02.springmail.presentation.dto.ChatDTO(
              c.id, c.subject, c.from.username,
              m.content, c.isRead, c.updatedAt,
              CASE WHEN f.id IS NOT NULL THEN true ELSE false END
          ) \s
          FROM Chat c
          LEFT JOIN Message m ON m.chat.id = c.id
          LEFT JOIN Favorite f ON f.chat.id = c.id AND f.user.email = :from
          WHERE c.from.email = :from AND LOWER(c.to.username) LIKE LOWER(%:to%)
          AND m.createdAt = (SELECT MAX(m2.createdAt) FROM Message m2 WHERE m2.chat.id = c.id)
          ORDER BY c.updatedAt DESC
          """)
    List<ChatDTO> getSentChatsByUsername(String from, String to, Pageable pageable);

    @Query("""
            SELECT new com.gabrielferreira02.springmail.presentation.dto.ChatDTO(
              c.id, c.subject, c.from.username,
              m.content, c.isRead, c.updatedAt,
              CASE WHEN f.id IS NOT NULL THEN true ELSE false END
          ) \s
          FROM Chat c
          LEFT JOIN Message m ON m.chat.id = c.id
          LEFT JOIN Favorite f ON f.chat.id = c.id AND f.user.email = :to
          WHERE c.to.email = :to AND LOWER(c.from.username) LIKE LOWER(%:from%)
          AND m.createdAt = (SELECT MAX(m2.createdAt) FROM Message m2 WHERE m2.chat.id = c.id)
          ORDER BY c.updatedAt DESC
          """)
    List<ChatDTO> getReceivedChatsByUsername(String from, String to, Pageable pageable);

}
