package ru.otus.hw.controllers.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import ru.otus.hw.dtos.AuthorDTO;
import ru.otus.hw.dtos.BookCommentDTO;
import ru.otus.hw.dtos.BookDTO;
import ru.otus.hw.dtos.GenreDTO;
import ru.otus.hw.services.BookCommentService;
import ru.otus.hw.services.CustomUserDetailsService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Класс BookCommentService")
@SpringBootTest
public class BookCommentServiceAuthzTest {

    @Autowired
    private BookCommentService bookCommentService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    private BookDTO bookDTO;
    private BookCommentDTO newCommentByUser1;

    @BeforeEach
    void setUp() {

        AuthorDTO authorDTO = new AuthorDTO(1L, "author");
        GenreDTO genreDTO = new GenreDTO(1L, "genre");
        bookDTO = BookDTO.builder()
                .id(1L)
                .title("book")
                .author(authorDTO)
                .genres(List.of(genreDTO))
                .build();

        newCommentByUser1 = BookCommentDTO.builder()
                .commentText("new_comment")
                .book(bookDTO)
                .build();
    }

    @Test
    @DisplayName("Должен успешно обновлять комментарий, если пользователь автор")
    void shouldUpdateCommentIfUserIsAuthor() {

        var user1Details = userDetailsService.loadUserByUsername("user1");
        setAuthentication(user1Details);

        var savedCommentByUser1 = bookCommentService.insert(newCommentByUser1);
        savedCommentByUser1.setCommentText("updated_comment");

        var actualCommentByUser1 = bookCommentService.update(savedCommentByUser1.getId(), savedCommentByUser1);
        assertThat(actualCommentByUser1).isEqualTo(savedCommentByUser1);
    }

    @Test
    @DisplayName("Должен выбрасывать AccessDeniedException, если пользователь не автор")
    void shouldThrowExceptionIfUserIsNotAuthor() {

        var user1Details = userDetailsService.loadUserByUsername("user1");
        setAuthentication(user1Details);

        var savedCommentByUser1 = bookCommentService.insert(newCommentByUser1);
        savedCommentByUser1.setCommentText("updated_comment");

        var user2Details = userDetailsService.loadUserByUsername("user2");
        setAuthentication(user2Details);

        assertThatThrownBy(() -> bookCommentService.update(savedCommentByUser1.getId(), savedCommentByUser1))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @DisplayName("Должен успешно удалять комментарий, если пользователь автор")
    void shouldDeleteCommentIfUserIsAuthor() {

        var user1Details = userDetailsService.loadUserByUsername("user1");
        setAuthentication(user1Details);

        var savedCommentByUser1 = bookCommentService.insert(newCommentByUser1);
        bookCommentService.deleteById(savedCommentByUser1.getId());

        assertThat(bookCommentService.findById(savedCommentByUser1.getId())).isEmpty();
    }

    @Test
    @DisplayName("Должен выбрасывать AccessDeniedException при удалении, если пользователь не автор")
    void shouldThrowExceptionWhenDeletingIfUserIsNotAuthor() {

        var user1Details = userDetailsService.loadUserByUsername("user1");
        setAuthentication(user1Details);

        var savedCommentByUser1 = bookCommentService.insert(newCommentByUser1);

        var user2Details = userDetailsService.loadUserByUsername("user2");
        setAuthentication(user2Details);

        assertThatThrownBy(() -> bookCommentService.deleteById(savedCommentByUser1.getId()))
                .isInstanceOf(AccessDeniedException.class);
    }

    private void setAuthentication(UserDetails userDetails) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        userDetails.getUsername(),
                        userDetails.getPassword(),
                        userDetails.getAuthorities()));
    }
}
