package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dtos.BookDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @Override
    public Optional<BookDTO> findById(long id) {
        var book = bookRepository.findById(id);
        return book.map(BookMapper::toBookDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Book> findBookById(long id) {
        return bookRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDTO> findAll() {
        List<Book> books = bookRepository.findAll();
        return BookMapper.toBookDTOList(books);
    }

    @Transactional
    @Override
    public BookDTO insert(String title, long authorId, Set<Long> genresIds) {
        Book book = save(0, title, authorId, genresIds);
        return BookMapper.toBookDTO(book);
    }

    @Transactional
    @Override
    public BookDTO update(long id, String title, long authorId, Set<Long> genresIds) {
        Book book = save(id, title, authorId, genresIds);
        return BookMapper.toBookDTO(book);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private Book save(long id, String title, long authorId, Set<Long> genresIds) {
        if (isEmpty(genresIds)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }
        var genres = genreRepository.findAllByIds(genresIds);
        if (isEmpty(genres) || genresIds.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }

        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));

        var book = new Book(id, title, author, genres);
        return bookRepository.save(book);
    }
}
