package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaAuthorRepository implements AuthorRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<Author> findAll() {
        String jpql = "select a from Author a";
        TypedQuery<Author> query = entityManager.createQuery(jpql, Author.class);
        return query.getResultList();
    }

    @Override
    public Optional<Author> findById(long id) {
        return Optional.ofNullable(entityManager.find(Author.class, id));
    }

    @Override
    public Author save(Author author) {
        if (author.getId() == 0) {
            entityManager.persist(author);
            return author;
        }
        return entityManager.merge(author);
    }

    @Override
    public void deleteById(long id) {
        Optional<Author> optionalAuthor = findById(id);
        optionalAuthor.ifPresent(author -> entityManager.remove(author));
    }
}
