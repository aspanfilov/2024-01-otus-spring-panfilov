package ru.otus.hw.services;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.sql.SqlBooksGenres;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BooksGenresCacheLoaderService {

    private final EntityManager entityManager;

    private final CacheService cacheService;

    public void loadBooksGenresCache() {
        List<SqlBooksGenres> booksGenres = entityManager
                .createQuery("select bg from SqlBooksGenres bg", SqlBooksGenres.class)
                .getResultList();

        booksGenres.forEach(bg -> cacheService.addGenreToBook(bg.getBookId(), bg.getGenreId()));
    }

}
