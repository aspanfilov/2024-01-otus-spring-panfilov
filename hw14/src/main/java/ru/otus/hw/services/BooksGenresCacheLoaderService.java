package ru.otus.hw.services;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.sql.SqlBooksGenres;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BooksGenresCacheLoaderService {

    private final EntityManager entityManager;

    private final Map<Long, List<Long>> booksGenresCache;

    public void loadBooksGenresCache() {
        List<SqlBooksGenres> booksGenres = entityManager
                .createQuery("select bg from SqlBooksGenres bg", SqlBooksGenres.class)
                .getResultList();

        booksGenres.forEach(bg -> {
            booksGenresCache.computeIfAbsent(bg.getBookId(), k -> new ArrayList<>())
                    .add(bg.getGenreId());
        });
    }

}
