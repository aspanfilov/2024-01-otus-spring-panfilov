package ru.otus.hw.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.Set;

@Component
@RequiredArgsConstructor
public class BookHandlerImpl implements BookHandler {

    private final BookRepository bookRepository;

    private final BookRepositoryCustomImpl bookRepositoryCustom;

    private final BookGenreRefRepository bookGenreRefRepository;

    @Override
    public Flux<BookDetailDTO> getBooks() {
        return bookRepositoryCustom.findAll();
    }

    @Override
    public Mono<ResponseEntity<Book>> getBook(Long id) {
        return bookRepositoryCustom.findById(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @Transactional
    @Override
    public Mono<ResponseEntity<BookBasicDto>> createBook(BookReferenceDTO bookReferenceDTO) {
        Book book = BookMapper.toNewEntity(bookReferenceDTO);
        return bookRepository.save(book)
                .flatMap(savedBook -> saveBookGenresAndReturnBook(savedBook, bookReferenceDTO.getGenreIds()))
                .map(BookMapper::toBookBasicDto)
                .map(ResponseEntity::ok);
    }

    @Transactional
    @Override
    public Mono<ResponseEntity<BookBasicDto>> updateBook(Long id, BookReferenceDTO bookReferenceDTO) {
        Book book = BookMapper.toEntity(bookReferenceDTO);
        return bookRepository.existsById(id)
                .mapNotNull(exists -> exists ? book : null)
                .flatMap(bookRepository::save)
                .flatMap(savedBook -> saveBookGenresAndReturnBook(savedBook, bookReferenceDTO.getGenreIds()))
                .map(BookMapper::toBookBasicDto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    private Mono<Book> saveBookGenresAndReturnBook(Book savedBook, Set<Long> genreIds) {
        List<BookGenreRef> bookGenreRefs = genreIds.stream()
                .map(genreId -> new BookGenreRef(savedBook.getId(), genreId))
                .toList();
        return bookGenreRefRepository.deleteByBookId(savedBook.getId())
                .thenMany(bookGenreRefRepository.saveAll(bookGenreRefs))
                .then(Mono.just(savedBook));
    }

    @Transactional
    @Override
    public Mono<ResponseEntity<Void>> deleteBook(Long id) {
        return bookGenreRefRepository.deleteByBookId(id)
                .then(bookRepository.deleteById(id))
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

}
