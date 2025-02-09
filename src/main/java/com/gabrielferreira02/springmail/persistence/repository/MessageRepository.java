package com.gabrielferreira02.springmail.persistence.repository;

import com.gabrielferreira02.springmail.persistence.entity.Message;
import com.gabrielferreira02.springmail.presentation.dto.MessageDTO;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    @Query("""
            SELECT new com.gabrielferreira02.springmail.presentation.dto.MessageDTO(
                m.sender.username,
                m.sender.email,
                m.createdAt,
                m.content
            ) FROM Message m WHERE m.chat.id = :chatId
            """)
    List<MessageDTO> findAllMessagesByChatId(UUID chatId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Message m WHERE m.chat.id IN (SELECT c.id FROM Chat c WHERE c.from.id = :userId OR c.to.id = :userId)")
    void deleteByChatFromUser(UUID userId);

    @Query("""
            SELECT m FROM Message m WHERE m.chat.id = :id ORDER BY m.createdAt DESC LIMIT 1
            """)
    Message getLastMessageFromChat(UUID id);
}
