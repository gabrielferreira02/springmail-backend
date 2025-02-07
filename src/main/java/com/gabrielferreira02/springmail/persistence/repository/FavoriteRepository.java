package com.gabrielferreira02.springmail.persistence.repository;

import com.gabrielferreira02.springmail.persistence.entity.Favorite;
import com.gabrielferreira02.springmail.presentation.dto.ChatDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface FavoriteRepository extends JpaRepository<Favorite, UUID> {

    @Query("""
            SELECT f FROM Favorite f WHERE f.chat.id = :chatId AND f.user.id = :userId
            """)
    Favorite findFavoriteByChatIdAndUserId(UUID chatId, UUID userId);


    @Query("""
             SELECT new com.gabrielferreira02.springmail.presentation.dto.ChatDTO(
                c.id, c.subject, c.from.username,
                m.content, c.isRead, c.updatedAt,
                true
            )\s
            FROM Chat c
            JOIN Favorite f ON f.chat.id = c.id
            LEFT JOIN Message m ON m.chat.id = c.id
            WHERE f.user.email = :userEmail
            AND m.createdAt = (SELECT MAX(m2.createdAt) FROM Message m2 WHERE m2.chat.id = c.id)
            ORDER BY c.updatedAt DESC
            """)
    List<ChatDTO> findFavorites(String userEmail, Pageable pageable);

    @Query("""
             SELECT new com.gabrielferreira02.springmail.presentation.dto.ChatDTO(
                c.id, c.subject, c.from.username,
                m.content, c.isRead, c.updatedAt,
                true
            )\s
            FROM Chat c
            JOIN Favorite f ON f.chat.id = c.id
            LEFT JOIN Message m ON m.chat.id = c.id
            WHERE f.user.email = :userEmail AND (LOWER(c.from.username) LIKE LOWER(%:username%) OR LOWER(c.to.username) LIKE LOWER(%:username%))
            AND m.createdAt = (SELECT MAX(m2.createdAt) FROM Message m2 WHERE m2.chat.id = c.id)
            ORDER BY c.updatedAt DESC
            """)
    List<ChatDTO> findFavoritesByUsername(String userEmail, String username, Pageable pageable);
}
