package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dtos.BookDetailDTO;
import ru.otus.hw.dtos.BookReferenceDTO;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookGenreRef;
import ru.otus.hw.repositories.BookGenreRefRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.BookRepositoryCustomImpl;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookHandlerImpl implements BookHandler {

    private final BookRepository bookRepository;

    private final BookRepositoryCustomImpl bookRepositoryCustom;

    private final BookGenreRefRepository bookGenreRefRepository;

    private final Logger logger = LoggerFactory.getLogger(BookHandlerImpl.class);

    @Override
    public Flux<BookDetailDTO> findAll() {
        return bookRepositoryCustom.findAll();
    }

//    @Override
//    public Mono<Book> saveBook(Mono<BookRequestDTO> bookRequestDto) {
//        Mono<Book> a = bookRequestDto.flatMap(bookRequest -> {
//            Book book = BookMapper.toEntity(bookRequest);
//            return bookRepository.save(book)
//                    .flatMap(savedBook -> {
//                        List<BookGenreRef> bookGenreRefs = bookRequest.getGenreIds().stream()
//                                .map(genreId -> new BookGenreRef(savedBook.getId(), genreId))
//                                .collect(Collectors.toList());
//                        return Flux.fromIterable(bookGenreRefs)
//                                .flatMap(bookGenreRefRepository::save)
//                                .then(Mono.just(savedBook));
//                    });
//        });
//
//        return a;
//    }

    @Override
    public Mono<Book> saveBook(Mono<BookReferenceDTO> bookRequestDto) {
        logger.info("Starting book save process");
        return bookRequestDto.flatMap(bookRequest -> {
            logger.info("Mapping DTO to entity");
            Book book = BookMapper.toEntity(bookRequest);
            return bookRepository.save(book)
                    .flatMap(savedBook -> {
                        logger.info("Book saved with ID: {}", savedBook.getId());
                        List<BookGenreRef> bookGenreRefs = bookRequest.getGenreIds().stream()
                                .map(genreId -> new BookGenreRef(savedBook.getId(), genreId))
                                .collect(Collectors.toList());
                        logger.info("Saving book-genre references");
                        return Flux.fromIterable(bookGenreRefs)
                                .flatMap(bookGenreRefRepository::save)
                                .then(Mono.just(savedBook));
                    });
        });
    }
}
