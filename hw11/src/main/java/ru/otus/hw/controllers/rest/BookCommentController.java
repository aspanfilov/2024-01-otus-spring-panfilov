package ru.otus.hw.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.BookComment;
import ru.otus.hw.repositories.BookCommentRepository;

@RestController
@RequiredArgsConstructor
public class BookCommentController {

    private final BookCommentRepository bookCommentRepository;

    @GetMapping("/api/v1/books/{bookId}/comments")
    public Flux<BookComment> getBookComments(@PathVariable("bookId") Long bookId) {
        return bookCommentRepository.findAllByBookId(bookId);
    }

    @GetMapping("/api/v1/books/{bookid}/comments/{bookCommentId}")
    public Mono<ResponseEntity<BookComment>> getBookComment(@PathVariable("bookCommentId") Long bookCommentId) {
        return bookCommentRepository.findById(bookCommentId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PostMapping("/api/v1/books/{bookId}/comments")
    public Mono<ResponseEntity<BookComment>> createBookComment(@PathVariable("bookId") Long bookId,
                                                               @RequestBody Mono<BookComment> bookComment) {
        //todo при открытии страницы комментариев к книге должна отображаться инфа по книге

        return bookComment.flatMap(bc -> {
                    BookComment newBookComment = new BookComment(null, bc.getCommentText(), bookId);
                    return bookCommentRepository.save(newBookComment);
                })
                .map(ResponseEntity::ok);
    }

    @PutMapping("/api/v1/books/{bookId}/comments/{bookCommentId}")
    public Mono<ResponseEntity<BookComment>> updateBookComment(@PathVariable("bookId") Long bookId,
                                                               @PathVariable("bookCommentId") Long bookCommentId,
                                                               @RequestBody Mono<BookComment> bookComment) {
        return bookComment.flatMap(bc -> bookCommentRepository.findById(bookCommentId)
                        .flatMap(existingBookComment -> {
                            BookComment updatedBookComment = new BookComment(bookCommentId, bc.getCommentText(), bookId);
                            return bookCommentRepository.save(updatedBookComment);
                        }))
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }


    @DeleteMapping("/api/v1/books/{bookId}/comments/{bookCommentId}")
    public Mono<ResponseEntity<Void>> deleteBookComment(@PathVariable("bookId") Long bookId,
                                                        @PathVariable("bookCommentId") Long bookCommentId) {
        return bookCommentRepository.deleteById(bookCommentId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
