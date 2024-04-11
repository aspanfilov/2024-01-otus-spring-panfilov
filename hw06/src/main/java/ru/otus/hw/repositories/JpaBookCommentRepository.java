package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.BookComment;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaBookCommentRepository implements BookCommentRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<BookComment> findAllByBookId(long bookId) {
        String jpql = "select bc from BookComment bc where bc.book.id = :bookId";
        TypedQuery<BookComment> query = entityManager.createQuery(jpql, BookComment.class);
        query.setParameter("bookId", bookId);

        return query.getResultList();
    }

    @Override
    public Optional<BookComment> findById(long id) {
        return Optional.ofNullable(entityManager.find(BookComment.class, id));
    }

    @Override
    public BookComment save(BookComment bookComment) {
        if (bookComment.getId() == 0) {
            entityManager.persist(bookComment);
            return bookComment;
        }
        return entityManager.merge(bookComment);
    }

    @Override
    public void deleteById(long id) {
        Optional<BookComment> optionalBookComment = findById(id);
        optionalBookComment.ifPresent(bookComment -> entityManager.remove(bookComment));
    }
}
