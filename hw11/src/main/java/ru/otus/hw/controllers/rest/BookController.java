package ru.otus.hw.controllers.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dtos.book.BookBasicDto;
import ru.otus.hw.dtos.book.BookDetailDTO;
import ru.otus.hw.dtos.book.BookReferenceDTO;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookGenreRef;
import ru.otus.hw.repositories.BookGenreRefRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.BookRepositoryCustomImpl;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;

    private final BookRepositoryCustomImpl bookRepositoryCustom;

    private final BookGenreRefRepository bookGenreRefRepository;

    @GetMapping("/api/v1/books")
    public Flux<BookDetailDTO> getBooks() {
        return bookRepositoryCustom.findAll();
    }

    @GetMapping("/api/v1/books/{id}")
    public Mono<ResponseEntity<Book>> getBook(@PathVariable("id") Long id) {
        return bookRepositoryCustom.findById(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PostMapping("/api/v1/books")
    @Transactional
    public Mono<ResponseEntity<BookBasicDto>> createBook(@RequestBody @Valid Mono<BookReferenceDTO> bookReferenceDTO) {
        return bookReferenceDTO.flatMap(bookRequest -> {
            Book book = BookMapper.toEntity(bookRequest);
            return bookRepository.save(book)
                    .flatMap(savedBook -> {
                        List<BookGenreRef> bookGenreRefs = bookRequest.getGenreIds().stream()
                                .map(genreId -> new BookGenreRef(savedBook.getId(), genreId))
                                .collect(Collectors.toList());
                        return Flux.fromIterable(bookGenreRefs)
                                .flatMap(bookGenreRefRepository::save)
                                .then(Mono.just(savedBook));
                    })
                    .map(BookMapper::toBookBasicDto)
                    .map(ResponseEntity::ok);
        });
    }

    @PutMapping("/api/v1/books/{id}")
    @Transactional
    public Mono<ResponseEntity<BookBasicDto>> updateBook(@PathVariable Long id,
                                                 @RequestBody @Valid Mono<BookReferenceDTO>  bookReferenceDTO) {
        return bookReferenceDTO.flatMap(bookRequest ->
                bookRepository.findById(id)
                        .flatMap(existingBook -> {
                            Book updatedBook = BookMapper.toEntity(bookRequest);
                            return bookRepository.save(updatedBook)
                                    .flatMap(savedBook -> bookGenreRefRepository.deleteByBookId(existingBook.getId())
                                            .thenMany(Flux.fromIterable(bookRequest.getGenreIds())
                                                    .map(genreId -> new BookGenreRef(savedBook.getId(), genreId))
                                                    .flatMap(bookGenreRefRepository::save))
                                            .then(Mono.just(savedBook)));
                        })
                        .map(BookMapper::toBookBasicDto)
                        .map(ResponseEntity::ok)
                        .switchIfEmpty(Mono.just(ResponseEntity.notFound().build())));
    }

    @DeleteMapping("/api/v1/books/{id}")
    @Transactional
    public Mono<ResponseEntity<Void>> deleteBook(@PathVariable("id") Long id) {
        return bookGenreRefRepository.deleteByBookId(id)
                .then(bookRepository.deleteById(id))
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

}