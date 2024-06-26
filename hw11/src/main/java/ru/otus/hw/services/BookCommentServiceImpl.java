package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookCommentServiceImpl implements BookCommentService {
//    private final BookCommentRepository bookCommentRepository;
//
//    private final BookService bookService;
//
//    @Transactional(readOnly = true)
//    @Override
//    public List<BookCommentDTO> findAllByBookId(long bookId) {
//        bookService.findById(bookId);
//        return bookCommentRepository.findAllByBookId(bookId).stream()
//        .map(BookCommentMapper::toBookCommentDTO).toList();
//    }
//
//    @Transactional(readOnly = true)
//    @Override
//    public Optional<BookCommentDTO> findById(long id) {
//        return bookCommentRepository.findById(id).map(BookCommentMapper::toBookCommentDTO);
//    }
//
//    @Transactional
//    @Override
//    public BookCommentDTO insert(long bookId, String commentText) {
//        BookComment bookComment = save(0, bookId, commentText);
//        return BookCommentMapper.toBookCommentDTO(bookComment);
//    }
//
//    @Transactional
//    @Override
//    public BookCommentDTO update(long id, long bookId, String commentText) {
//        BookComment bookComment = save(id, bookId, commentText);
//        return BookCommentMapper.toBookCommentDTO(bookComment);
//    }
//
//    @Transactional
//    @Override
//    public void deleteById(long id) {
//        bookCommentRepository.deleteById(id);
//    }
//
//    private BookComment save(long id, long bookId, String commentText) {
//        Book book = bookService.findBookById(bookId)
//                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
//        var bookComment = new BookComment(id, commentText, book);
//        return bookCommentRepository.save(bookComment);
//    }
}
