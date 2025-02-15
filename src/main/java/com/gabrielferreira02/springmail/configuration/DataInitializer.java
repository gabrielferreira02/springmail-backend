package com.gabrielferreira02.springmail.configuration;

import com.gabrielferreira02.springmail.persistence.entity.Chat;
import com.gabrielferreira02.springmail.persistence.entity.Favorite;
import com.gabrielferreira02.springmail.persistence.entity.Message;
import com.gabrielferreira02.springmail.persistence.entity.User;
import com.gabrielferreira02.springmail.persistence.repository.ChatRepository;
import com.gabrielferreira02.springmail.persistence.repository.FavoriteRepository;
import com.gabrielferreira02.springmail.persistence.repository.MessageRepository;
import com.gabrielferreira02.springmail.persistence.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    @Transactional
    CommandLineRunner init(
            UserRepository userRepository,
            ChatRepository chatRepository,
            FavoriteRepository favoriteRepository,
            MessageRepository messageRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
//            USERS
            User user1 = userRepository.save(
                    new User(
                            null,
                            "Springmail Team",
                            "springmail@springmail.com",
                            passwordEncoder.encode("12345678")
                    )
            );
            User user2 = userRepository.save(
                    new User(
                            null,
                            "Gabriel Ferreira",
                            "gabrielf@springmail.com",
                            passwordEncoder.encode("12345678")
                    )
            );
            User user3 = userRepository.save(
                    new User(
                            null,
                            "Mar Acevedo",
                            "maracevedo@springmail.com",
                            passwordEncoder.encode("12345678")
                    )
            );
            User user4 = userRepository.save(
                    new User(
                            null,
                            "Momo Art",
                            "momoart@springmail.com",
                            passwordEncoder.encode("12345678")
                    )
            );
//            CHATS
            Chat chat1 = chatRepository.save(
                new Chat(
                        null,
                        "Bem vindo ao springmail",
                        user1,
                        user2,
                        false,
                        List.of(),
                        null, null
                )
            );
            Chat chat2 = chatRepository.save(
                    new Chat(
                            null,
                            "Uma mensagem qualquer",
                            user3,
                            user2,
                            false,
                            List.of(),
                            null, null
                    )
            );
            Chat chat3 = chatRepository.save(
                    new Chat(
                            null,
                            "Uma mensagem qualquer",
                            user2,
                            user4,
                            false,
                            List.of(),
                            null, null
                    )
            );
            Chat chat4 = chatRepository.save(
                    new Chat(
                            null,
                            "Uma mensagem qualquer",
                            user2,
                            user3,
                            false,
                            List.of(),
                            null, null
                    )
            );
            Chat chat5 = chatRepository.save(
                    new Chat(
                            null,
                            "Uma mensagem qualquer",
                            user4,
                            user2,
                            false,
                            List.of(),
                            null, null
                    )
            );
            Chat chat6 = chatRepository.save(
                    new Chat(
                            null,
                            "Sobre o projeto do springmail",
                            user1,
                            user2,
                            false,
                            List.of(),
                            null, null
                    )
            );
            Chat chat7 = chatRepository.save(
                    new Chat(
                            null,
                            "Uma mensagem qualquer",
                            user2,
                            user4,
                            false,
                            List.of(),
                            null, null
                    )
            );
            Chat chat8 = chatRepository.save(
                    new Chat(
                            null,
                            "Uma mensagem qualquer",
                            user2,
                            user3,
                            false,
                            List.of(),
                            null, null
                    )
            );
//            CHAT 1
            messageRepository.save(
                    new Message(null, chat1, user1,
                            "Seja bem vindo ao SpringMail! \n Utilize nossa plataforma para se comunicar com outras pessoas",
                            null)
            );
//            CHAT 2
            messageRepository.save(
                    new Message(null, chat2, user3,
                            "Uma mensagem para criar um hat",
                            null)
            );
            messageRepository.save(
                    new Message(null, chat2, user2,
                            "Mensagem de resposta",
                            null)
            );
            messageRepository.save(
                    new Message(null, chat2, user3,
                            "Mensagem de resposta",
                            null)
            );
            messageRepository.save(
                    new Message(null, chat2, user3,
                            "Mensagem de resposta",
                            null)
            );
//      CHAT 3
            messageRepository.save(
                    new Message(null, chat3, user2,
                            "Uma mensagem para criar um hat",
                            null)
            );
            messageRepository.save(
                    new Message(null, chat3, user4,
                            "Mensagem de resposta",
                            null)
            );
            messageRepository.save(
                    new Message(null, chat3, user2,
                            "Mensagem de resposta",
                            null)
            );
//            CHAT 4
            messageRepository.save(
                    new Message(null, chat4, user2,
                            "Mensagem de resposta",
                            null)
            );
            messageRepository.save(
                    new Message(null, chat4, user3,
                            "Mensagem de resposta",
                            null)
            );
            messageRepository.save(
                    new Message(null, chat4, user2,
                            "Mensagem de resposta",
                            null)
            );
            messageRepository.save(
                    new Message(null, chat4, user3,
                            "Mensagem de resposta",
                            null)
            );
            messageRepository.save(
                    new Message(null, chat2, user2,
                            "Mensagem de resposta",
                            null)
            );
//            CHAT 5
            messageRepository.save(
                    new Message(null, chat5, user4,
                            "Mensagem para criar um chat",
                            null)
            );
            messageRepository.save(
                    new Message(null, chat4, user3,
                            "Mensagem de resposta",
                            null)
            );
//            CHAT 6
            messageRepository.save(
                    new Message(null, chat6, user1,
                            "Se gostou do projeto deixe uma estrela para ajudar meu perfil. \n Desenvolvido em Angular + Typescript no frontend utilizando tailwindcss para  estilização e Java + Spring no backend para gerenciar as regras de negócio do sistema",
                            null)
            );
//            CHAT 7
            messageRepository.save(
                    new Message(null, chat7, user2,
                            "Mensagem de resposta",
                            null)
            );
            messageRepository.save(
                    new Message(null, chat7, user2,
                            "Mensagem de resposta",
                            null)
            );
            messageRepository.save(
                    new Message(null, chat7, user4,
                            "Mensagem de resposta",
                            null)
            );
            messageRepository.save(
                    new Message(null, chat7, user2,
                            "Mensagem de resposta",
                            null)
            );
            messageRepository.save(
                    new Message(null, chat7, user4,
                            "Mensagem de resposta",
                            null)
            );
//            CHAT 8
            messageRepository.save(
                    new Message(null, chat8, user2,
                            "Mensagem de resposta",
                            null)
            );
            messageRepository.save(
                    new Message(null, chat8, user3,
                            "Mensagem de resposta",
                            null)
            );
            messageRepository.save(
                    new Message(null, chat8, user2,
                            "Mensagem de resposta",
                            null)
            );
            messageRepository.save(
                    new Message(null, chat8, user3,
                            "Mensagem de resposta",
                            null)
            );
            messageRepository.save(
                    new Message(null, chat8, user2,
                            "Mensagem de resposta",
                            null)
            );
//            FAVORITES
            favoriteRepository.save(
                    new Favorite(
                            null,
                            chat2,
                            user2
                    )
            );

            favoriteRepository.save(
                    new Favorite(
                            null,
                            chat4,
                            user2
                    )
            );
        };
    }
}
